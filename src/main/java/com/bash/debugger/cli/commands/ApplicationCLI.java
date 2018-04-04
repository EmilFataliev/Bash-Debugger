package com.bash.debugger.cli.commands;

public enum ApplicationCLI {
  DEBUG("-d"),
  HELP("-h");

  private final String argument;

  ApplicationCLI(String argument) {
    this.argument = argument;
  }

  public String getArgument() {
    return argument;
  }
}
