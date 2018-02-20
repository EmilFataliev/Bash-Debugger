package ru.emil.bashdb;

public class App {

  private static final String BASH_ENVIRONMENT = "/usr/local/bin/bash";

  public static void main(String[] args) {
    BashScriptRunner bashScriptRunner = new BashScriptRunner(BASH_ENVIRONMENT,
        "/Users/emilfataliev/Desktop/bashdb/src/main/resources/script_with_reading.sh");

    bashScriptRunner.run();
  }
}
