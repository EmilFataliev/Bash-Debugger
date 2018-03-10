package ru.emil.bashdb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.emil.bashdb.script.entity.Script;

public class BashScriptExecutor {

  private static final Logger logger = LoggerFactory.getLogger(BashScriptExecutor.class);

  private final Script script;

  /* package private */
  public BashScriptExecutor(final Script script) {
    this.script = script;
  }

  public String run(boolean redirectToConsole) throws IOException {
    final StringBuilder scriptOutput = new StringBuilder();
    final ProcessBuilder processBuilder = new ProcessBuilder(script.getBashEnvironment(), "-c",
        script.getTracedContent());

    if (redirectToConsole) {
      processBuilder
          .redirectErrorStream(true)
          .redirectOutput(Redirect.INHERIT);
    }

    final Process process = processBuilder.start();

    try (final BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(process.getInputStream()))) {
      scriptOutput.append(bufferedReader.readLine());
    }

    return scriptOutput.toString();
  }

  public void debug() throws IOException {
    System.out.println(
        String.format(
            "Executing script %s in path %s:",
            script.getPath().toFile().getName(),
            script.getPath().toFile().getParent()
        )
    );

    final Process process = new ProcessBuilder(script.getBashEnvironment(), "-c",
        script.getTracedContent())
        .redirectErrorStream(true)
        .redirectOutput(Redirect.INHERIT)
        .start();

    final BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(process.getOutputStream()));

    final Scanner scanner = new Scanner(System.in);

    try {
      while (process.isAlive()) {
        final String userCommand = scanner.next();

        writer.write(userCommand + "\n");
        writer.flush();
      }

    } catch (Exception e) {
      writer.close();
      logger.error("failed execute " + script.getPath() + " ", e);
    }

  }

}
