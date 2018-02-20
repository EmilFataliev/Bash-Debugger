package ru.emil.bashdb;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BashScriptRunner {

  private static final Logger logger = LoggerFactory.getLogger(BashScriptRunner.class);

  private final String bashEnvironment;
  private final String scriptPath;

  /* package private */ BashScriptRunner(String bashEnviroment, String scriptPath) {
    this.bashEnvironment = bashEnviroment;
    this.scriptPath = scriptPath;
  }

  private String readScript() throws IOException {
    Path path = Paths.get(scriptPath);
    StringBuilder script = new StringBuilder();
    Stream<String> lines = Files.lines(path);
    lines.forEach(line -> script.append(line).append("\n"));
    script.deleteCharAt(script.length() - 1);
    lines.close();

    return script.toString();
  }


  public void run() throws IOException {

    Process process = new ProcessBuilder(bashEnvironment, "-c", readScript())
        .redirectErrorStream(true)
        .redirectOutput(Redirect.INHERIT)
        .start();

    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

    try {
      while (process.isAlive()) {
        writer.write(new Scanner(System.in).next() + "\n");
        writer.flush();
      }

    } catch (Exception e) {
      writer.close();
      logger.error("failed execute " + scriptPath + " ", e);
    }

  }

}
