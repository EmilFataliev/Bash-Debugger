package ru.emil.bashdb;

import ru.emil.bashdb.commands.ConsoleInterfaceCommands;
import ru.emil.bashdb.executor.api.ScriptExecutor;
import ru.emil.bashdb.executor.impl.ScriptExecutorImpl;
import ru.emil.bashdb.script.entity.Script;

public class BashDebuggerApp {

  public static void main(String[] args) {
    String scriptPath;
    String commandOption;

    System.getProperties().list(System.out);

    if (args.length > 1) {
      commandOption = args[0];
      scriptPath = args[1];
    } else {
      throw new IllegalArgumentException("Illegal arguments for script executing");
    }

    final Script script = new Script.ScriptBuilder()
        .withPath(scriptPath)
        .readContent()
        .handleContent()
        .build();

    final ScriptExecutor bashScriptExecutor = new ScriptExecutorImpl();

    if (commandOption.equals(ConsoleInterfaceCommands.DEBUG.getParam())) {
      bashScriptExecutor.execute(script);
    }

  }
}
