package com.bash.debugger.cli.commands;

public enum ApplicationCLI {
  DEBUG("-d", "[--script-path] debug mode"),
  BASH_PATH("-b",
      "[--bash-path] bash path"),
  HELP("-h", "print CLI help");

  private final String argument;
  private final String description;

  ApplicationCLI(final String argument, final String description) {
    this.argument = argument;
    this.description = description;
  }

  public String getArgument() {
    return argument;
  }

  public String getDescription() {
    return description;
  }

  public static String getHelp() {
    StringBuilder commandsWithDescription = new StringBuilder();

    for (ApplicationCLI command : ApplicationCLI.values()) {
      commandsWithDescription
          .append(Consts.TAB)
          .append(command.argument)
          .append(Consts.DELIMITER)
          .append(command.description)
          .append(System.lineSeparator());
    }

    return commandsWithDescription.toString();
  }
}
