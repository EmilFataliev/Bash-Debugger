package ru.emil.bashdb;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.emil.bashdb.script.entity.Script;

public class BashDebuggerApp {

  /**
   * Debug mode constants
   **/
  private static final boolean DEBUG_MODE = true;
  // Folder of scripts prepared for debug mode
  private static final String SCRIPTS_PATH = "debug/scripts/";
  // TODO: Should be determined in runtime
  private static final String BASH_ENVIRONMENT = "/usr/local/bin/bash";

  enum DebugScripts {
    MEDIUM_SCRIPT("medium_script.sh"),
    SIMPLE_LS_SCRIPT("simple_ls_script.sh"),
    SCRIPT_WITH_READING("script_with_reading.sh");

    private final String scriptName;

    DebugScripts(final String scriptName) {
      this.scriptName = scriptName;
    }

    public String getScriptFullName() {
      return SCRIPTS_PATH + scriptName;
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(BashDebuggerApp.class);

  public static void main(String[] args) {
    String scriptPath;

    try {

      // Determine script path
      if (DEBUG_MODE) {
        scriptPath = Objects
            .requireNonNull(
                ClassLoader
                    .getSystemClassLoader()
                    .getResource(DebugScripts.SCRIPT_WITH_READING.getScriptFullName())
            ).getPath();
      } else {
        if (args.length > 0) {
          scriptPath = args[0];
        } else {
          throw new IllegalArgumentException("Path to script not specified");
        }
      }

      final Script script = Script.of(
          Paths.get(scriptPath)
      );

      final BashScriptExecutor bashScriptExecutor = new BashScriptExecutor(
          BASH_ENVIRONMENT,
          script
      );

      bashScriptExecutor.debug();
    } catch (IOException e) {
      logger.error("Error during script executing: ", e);
    }
  }
}
