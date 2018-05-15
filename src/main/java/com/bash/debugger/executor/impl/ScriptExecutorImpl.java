package com.bash.debugger.executor.impl;

import com.bash.debugger.env.BashDebuggerUserSystemInfo;
import com.bash.debugger.executor.api.ScriptExecutor;
import com.bash.debugger.script.entity.Script;
import com.google.common.base.Preconditions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptExecutorImpl implements ScriptExecutor {

  private static final Logger log = LoggerFactory.getLogger(ScriptExecutor.class);
  private static final String TRACING_SCRIPT_NAME = "main/bash_db.sh";

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final int SLEEP_MILLIS = 1000;

  @Override
  public void execute(final Script script) {
    log.debug(
        String.format(
            "Executing script %s in path %s:",
            script.getPath().toFile().getName(),
            script.getPath().toFile().getAbsolutePath()
        )
    );

    final InputStreamReader inputStreamReader = new InputStreamReader(ClassLoader
        .getSystemClassLoader()
        .getResourceAsStream(TRACING_SCRIPT_NAME));

    File traceScript = null;
    FileWriter writer = null;

    try {
      traceScript = File.createTempFile("bash_db", ".sh");
      writer = new FileWriter(traceScript);
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }

    Preconditions.checkState(Objects.requireNonNull(traceScript).setExecutable(true));
    Preconditions.checkState(Objects.requireNonNull(script.getPath().toFile()).setExecutable(true));

    try (final BufferedReader bashDbScriptReader = new BufferedReader(inputStreamReader)) {
      String lines = bashDbScriptReader
          .lines()
          .reduce((acc, line) -> acc + line + System.lineSeparator())
          .orElseThrow(IllegalAccessError::new);

      Objects.requireNonNull(writer).write(lines);
      writer.flush();
      writer.close();

    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }

    ShellThread shellThread = new ShellThread();
    shellThread.start();

    doSleep(SLEEP_MILLIS);

    System.out.println(
        ANSI_GREEN
            + "Start debugging "
            + script.getPath().getFileName()
            + ANSI_RESET
    );

    PrintWriter printWriter = new PrintWriter(shellThread.stdOutput);

    log.debug(traceScript.getAbsolutePath() + " " + script.getPath());

    printWriter.println(traceScript.getAbsolutePath() + " " + script.getPath());
    printWriter.flush();

    final InputStreamReader inputStream = new InputStreamReader(shellThread.inputStream);
    final InputStreamReader errorStream = new InputStreamReader(shellThread.errorStream);
    final InputStreamReader consoleStream = new InputStreamReader(System.in);

    try {
      while (shellThread.isAlive()) {
        handleOut(shellThread, inputStream);

        handleOut(shellThread, errorStream);
        if (consoleStream.ready()) {
          final BufferedReader bufferedReader = new BufferedReader(consoleStream);
          final String cmd = bufferedReader.readLine();

          if ("exit".equals(cmd)) {
            shellThread.exit();
          } else {
            final PrintWriter consolePrinter = new PrintWriter(shellThread.stdOutput);
            consolePrinter.println(cmd);
            consolePrinter.flush();
            shellThread.stdOutput.flush();
          }
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }

  }

  private void handleOut(ShellThread shellThread, InputStreamReader inputStream)
      throws IOException {
    if (inputStream.ready()) {
      final BufferedReader bufferedReader = new BufferedReader(inputStream);
      while (bufferedReader.ready()) {
        final String line = bufferedReader.readLine();
        if ("exit".equals(line)) {
          shellThread.exit();
          System.exit(0);
        } else {
          System.out.println(line);
        }
      }
    }
  }

  private void doSleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ignored) {
    }
  }

  private class ShellThread extends Thread {

    private ProcessBuilder processBuilder;
    private Process process;
    private InputStream inputStream;
    private InputStream errorStream;
    private OutputStream stdOutput;

    public void run() {
      try {
        final BashDebuggerUserSystemInfo bashDebuggerUserSystemInfo = BashDebuggerUserSystemInfo
            .getInstance();

        processBuilder = new ProcessBuilder(
            Collections.singletonList(
                bashDebuggerUserSystemInfo.getBashEnvLocation().toString()
            )
        );

        process = processBuilder.start();

        stdOutput = process.getOutputStream();
        inputStream = process.getInputStream();
        errorStream = process.getErrorStream();

        int exitValue = process.waitFor();
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    }

    void exit() {
      process.destroy();
    }
  }
}



