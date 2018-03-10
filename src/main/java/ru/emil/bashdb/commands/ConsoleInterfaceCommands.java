package ru.emil.bashdb.commands;

public enum ConsoleInterfaceCommands {
  DEBUG("-d"),
  RUN("-r"),
  HELP("-h");

  private final String param;

  ConsoleInterfaceCommands(String param) {
    this.param = param;
  }

  public String getParam() {
    return param;
  }
}
