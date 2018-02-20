package ru.emil.bashdb;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private static final String BASH_ENVIRONMENT = "/usr/local/bin/bash";
  private static final Logger logger = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    BashScriptRunner bashScriptRunner = new BashScriptRunner(BASH_ENVIRONMENT,
        "/Users/emilfataliev/Desktop/bashdb/src/main/resources/script_with_reading.sh");

    try {
      bashScriptRunner.run();
    } catch (IOException e) {
      logger.error("Ошибка при выполнении скрипта", e);
    }
  }
}
