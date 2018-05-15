package com.bash.debugger;

import com.bash.debugger.cli.commands.ApplicationCLI;
import com.bash.debugger.cli.commands.ExecutionCLI;
import com.bash.debugger.env.BashDebuggerUserSystemInfo;
import com.bash.debugger.executor.api.ScriptExecutor;
import com.bash.debugger.executor.impl.ScriptExecutorImpl;
import com.bash.debugger.script.entity.Script;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BashDebuggerApp {

  private static final Logger log = LoggerFactory.getLogger(BashDebuggerApp.class);

  private static void printUsage() {
    System.out.println("Application command line options: ");
    System.out.println(ApplicationCLI.getHelp());
    System.out.println("Command line options in debug mode: ");
    System.out.println(ExecutionCLI.getHelp());
  }

  public static void main(String[] args) {
    Path scriptPath = null;
    Path bashPath = null;

    log.debug(Arrays.toString(args));
    if (args.length < 1) {
      printUsage();
      return;
    }

    for (int i = 0; i < args.length; i++) {
      final String arg = args[i];

      if (ApplicationCLI.HELP.getArgument().equals(arg)) {
        printUsage();
      }

      if (ApplicationCLI.DEBUG.getArgument().equals(arg)) {
        if (i + 1 < args.length) {
          scriptPath = Paths.get(args[++i]);
          log.debug("Script path declared ", scriptPath);
          continue;
        } else {
          printUsage();
        }
      }

      if (ApplicationCLI.BASH_PATH.getArgument().equals(arg)) {
        if (i + 1 < args.length) {
          bashPath = Paths.get(args[++i]);
          log.debug("Bash path declared ", bashPath);
        } else {
          printUsage();
        }
      }
    }

    if (Objects.nonNull(bashPath)) {
      BashDebuggerUserSystemInfo.getInstance().setBashEnvLocation(bashPath);
    }

    if (Objects.nonNull(scriptPath)) {
      final Script script = Script.withPath(scriptPath);
      final ScriptExecutor bashScriptExecutor = new ScriptExecutorImpl();

      try {
        bashScriptExecutor.execute(script);
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
  }
}
