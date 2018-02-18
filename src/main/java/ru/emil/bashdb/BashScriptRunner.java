package ru.emil.bashdb;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BashScriptRunner {

  private static final Logger logger = LoggerFactory.getLogger(BashScriptRunner.class);

  public static void run(String script) {

    try {

      ProcessBuilder builder = new ProcessBuilder("/usr/local/bin/bash", "-c", script);

      builder.redirectErrorStream(true);

      Process process = builder.start();
      Scanner scan = new Scanner(System.in);
      String line;

      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

      while (true) {
        line = reader.readLine();
        if (line != null) {
          System.out.println(line);
        } else {
          break;
        }

        String input = scan.nextLine();
        writer.write(input.trim() + "\n");
        writer.flush();
      }

    } catch (Exception e) {
      logger.error("failed execute " + script + " ", e);

    }

  }

}
