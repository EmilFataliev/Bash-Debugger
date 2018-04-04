package com.bash.debugger;

import com.bash.debugger.cli.commands.ApplicationCLI;
import com.bash.debugger.executor.api.ScriptExecutor;
import com.bash.debugger.executor.impl.ScriptExecutorImpl;
import com.bash.debugger.script.entity.Script;

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

    if (commandOption.equals(ApplicationCLI.DEBUG.getArgument())) {
      bashScriptExecutor.execute(script);
    }

  }
}
