package com.bash.debugger.executor.impl;

import com.bash.debugger.env.BashUserSystemInfo;
import com.bash.debugger.executor.api.ScriptExecutor;
import com.bash.debugger.script.entity.Script;
import java.io.BufferedReader;
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
  private static final String TRACING_SCRIPT_NAME = "tracing/xtrace.sh";

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

    final Path traceScript = Paths.get(
        Objects.requireNonNull(
            getClass()
                .getClassLoader()
                .getResource(TRACING_SCRIPT_NAME)
        ).getPath());

    List<String> commands = new ArrayList<>();
    commands.add(bashUserSystemInfo.getBashEnvLocation());

    ShellThread shellThread = new ShellThread();
    shellThread.start();

    doSleep(2000);

    PrintWriter printWriter = new PrintWriter(shellThread.stdOutput);
    printWriter.println(
        traceScript.toFile().getAbsolutePath() + " " + script.getPath().toAbsolutePath()
            .toString());
    printWriter.flush();

    InputStreamReader inputStream = new InputStreamReader(shellThread.inputStream);
    InputStreamReader errorStream = new InputStreamReader(shellThread.errorStream);
    InputStreamReader consoleStream = new InputStreamReader(System.in);

    try {
      while (shellThread.isAlive()) {
        if (inputStream.ready()) {
          BufferedReader bufferedReader = new BufferedReader(inputStream);
          System.out.println(bufferedReader.readLine());
        }

        if (errorStream.ready()) {
          BufferedReader bufferedReader = new BufferedReader(errorStream);
          System.out.println(bufferedReader.readLine());
        }
        if (consoleStream.ready()) {
          BufferedReader bufferedReader = new BufferedReader(consoleStream);
          printWriter.println(bufferedReader.readLine());
          printWriter.flush();
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



