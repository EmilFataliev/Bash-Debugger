package com.bash.debugger.cli.commands;

public enum ApplicationCLI {
  DEBUG("-d"),
  BASH_PATH("--bash-path"),
  HELP("-h");

  private final String argument;

  ApplicationCLI(String argument) {
    this.argument = argument;
  }

  public String getArgument() {
    return argument;
  }
}
