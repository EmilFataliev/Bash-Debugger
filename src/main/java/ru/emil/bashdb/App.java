package ru.emil.bashdb;

import java.io.IOException;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.emil.bashdb.script.proccessing.entity.Script;

public class App {

  private static final String BASH_ENVIRONMENT = "/usr/local/bin/bash";
  private static final Logger logger = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    try {
      final Script script = Script.of(
          Paths
              .get("/Users/emilfataliev/Desktop/bashdb/src/main/resources/script_with_reading.sh"));
      final BashScriptRunner bashScriptRunner = new BashScriptRunner(BASH_ENVIRONMENT, script);
      bashScriptRunner.run();
    } catch (IOException e) {
      logger.error("Ошибка при выполнении скрипта", e);
    }
  }
}
