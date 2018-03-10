package ru.emil.bashdb;

import java.io.IOException;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.emil.bashdb.commands.ConsoleInterfaceCommands;
import ru.emil.bashdb.script.entity.Script;

public class BashDebuggerApp {

  private static final Logger logger = LoggerFactory.getLogger(BashDebuggerApp.class);

  public static void main(String[] args) {
    String scriptPath;
    String commandOption;

    try {
      if (args.length > 1) {
        commandOption = args[0];
        scriptPath = args[1];
      } else {
        throw new IllegalArgumentException();
      }

      final Script script = Script.of(
          Paths.get(scriptPath)
      );

      final BashScriptExecutor bashScriptExecutor = new BashScriptExecutor(
          script
      );

      if (commandOption.equals(ConsoleInterfaceCommands.DEBUG.getParam())) {
        bashScriptExecutor.debug();
      } else {
        bashScriptExecutor.run(false);
      }
    } catch (IOException e) {
      logger.error("Error during script executing: ", e);
    }
  }
}
