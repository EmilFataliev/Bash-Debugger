package com.bash.debugger.cli.commands;

public enum ExecutionCLI {
  RUN("run", "continue script execution"),
  STOP("exit", "stop script execution"),
  VARIABLES("env", "get stack of variables in script"),
  VARIABLES_ALL("env -f", "get stack of variables"),
  HELP("help", "get all available commands");

  private final String command;
  private final String description;

  ExecutionCLI(final String command, final String description) {
    this.command = command;
    this.description = description;
  }

  public String getCommand() {
    return command;
  }

  public String getDescription() {
    return description;
  }

  public static String getHelp() {
    StringBuilder commandsWithDescription = new StringBuilder();

    for (ExecutionCLI command : ExecutionCLI.values()) {
      commandsWithDescription
          .append(Consts.TAB)
          .append(command.command)
          .append(Consts.DELIMITER)
          .append(command.description)
          .append(System.lineSeparator());
    }

    return commandsWithDescription.toString();
  }
}
