package ru.emil.bashdb.commands;

public enum UserInterfaceCommand {
  STEP("step"),
  RUN_TO_NEXT_BR("run"),
  STOP("stop"),
  HELP("help");

  private final String command;

  UserInterfaceCommand(final String command) {
    this.command = command;
  }

  public String getCommand() {
    return command;
  }
}
