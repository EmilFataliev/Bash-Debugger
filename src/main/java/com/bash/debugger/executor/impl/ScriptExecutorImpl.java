package com.bash.debugger.executor.impl;

import com.bash.debugger.env.BashUserSystemInfo;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptExecutorImpl implements ScriptExecutor {

  private static final Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);
  private static final String TRACING_SCRIPT_NAME = "tracing/bash_db.sh";

  @Override
  public void execute(final Script script) {
    logger.debug(
        String.format(
            "Executing script %s in path %s:",
            script.getPath().toFile().getName(),
            script.getPath().toFile().getParent()
        )
    );

    final BashUserSystemInfo bashUserSystemInfo = BashUserSystemInfo.INSTANCE.getInstance();

    final InputStreamReader inputStreamReader = new InputStreamReader(ClassLoader
        .getSystemClassLoader()
        .getResourceAsStream(TRACING_SCRIPT_NAME));

    File traceScript = null;
    FileWriter writer = null;

    try {
      traceScript = File.createTempFile("bash_db", ".sh");
      writer = new FileWriter(traceScript);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    Preconditions.checkState(Objects.requireNonNull(traceScript).setExecutable(true));

    try (final BufferedReader bashDbScriptReader = new BufferedReader(inputStreamReader)) {
      String lines = bashDbScriptReader
          .lines()
          .reduce((acc, line) -> acc + line + System.lineSeparator())
          .orElseThrow(IllegalAccessError::new);

      Objects.requireNonNull(writer).write(lines);
      writer.flush();
      writer.close();

    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }

    List<String> commands = new ArrayList<>();
    commands.add(bashUserSystemInfo.getBashEnvLocation());

    ShellThread shellThread = new ShellThread();
    shellThread.start();

    doSleep(2000);

    PrintWriter printWriter = new PrintWriter(shellThread.stdOutput);
    logger.info(traceScript.getPath() + " " + script.getPath());
    printWriter.println(traceScript.getPath() + " " + script.getPath());
    printWriter.flush();

    InputStreamReader inputStream = new InputStreamReader(shellThread.inputStream);
    InputStreamReader errorStream = new InputStreamReader(shellThread.errorStream);
    InputStreamReader consoleStream = new InputStreamReader(System.in);

    try {
      while (shellThread.isAlive()) {
        if (inputStream.ready()) {
          BufferedReader bufferedReader = new BufferedReader(inputStream);
          while (bufferedReader.ready()) {
            System.out.println(bufferedReader.readLine());
          }
        }

        if (errorStream.ready()) {
          BufferedReader bufferedReader = new BufferedReader(errorStream);
          while (bufferedReader.ready()) {
            System.out.println(bufferedReader.readLine());
          }
        }
        if (consoleStream.ready()) {
          BufferedReader bufferedReader = new BufferedReader(consoleStream);

          PrintWriter consolePrinter = new PrintWriter(shellThread.stdOutput);
          consolePrinter.println(bufferedReader.readLine());
          consolePrinter.flush();
          shellThread.stdOutput.flush();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void doSleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
    }
  }

  public class ShellThread extends Thread {

    InputStream inputStream;
    InputStream errorStream;
    OutputStream stdOutput;

    public void run() {
      try {
        final BashUserSystemInfo bashUserSystemInfo = BashUserSystemInfo.INSTANCE.getInstance();

        List<String> commands = new ArrayList<>();
        commands.add(bashUserSystemInfo.getBashEnvLocation());

        ProcessBuilder pb = new ProcessBuilder(commands);
        Process process = pb.start();

        stdOutput = process.getOutputStream();
        inputStream = process.getInputStream();
        errorStream = process.getErrorStream();

        int exitValue = process.waitFor();
      } catch (Throwable th) {
        th.printStackTrace();
      }
    }
  }
}



