package ru.emil.bashdb;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.emil.bashdb.script.proccessing.entity.Script;

class BashScriptRunner {

  private static final Logger logger = LoggerFactory.getLogger(BashScriptRunner.class);

  private final String bashEnvironment;
  private final Script script;

  /* package private */ BashScriptRunner(String bashEnviroment, Script script) {
    this.bashEnvironment = bashEnviroment;
    this.script = script;
  }

  public void run() throws IOException {
    System.out.println(String.format("Running script %s in path %s:", script.getPath().toFile().getName(),
        script.getPath().toFile().getCanonicalPath()));

    final Process process = new ProcessBuilder(bashEnvironment, "-c", script.getTracedContent())
        .redirectErrorStream(true)
        .redirectOutput(Redirect.INHERIT)
        .start();

    final BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(process.getOutputStream()));

    final Scanner scanner = new Scanner(System.in);

    try {
      while (process.isAlive()) {
        writer.write(scanner.next() + "\n");
        writer.flush();
      }

    } catch (Exception e) {
      writer.close();
      logger.error("failed execute " + script.getPath() + " ", e);
    }

  }

}
