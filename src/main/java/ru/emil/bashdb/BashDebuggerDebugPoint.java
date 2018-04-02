package ru.emil.bashdb;

import java.util.Objects;
import ru.emil.bashdb.executor.api.ScriptExecutor;
import ru.emil.bashdb.executor.impl.ScriptExecutorImpl;
import ru.emil.bashdb.script.entity.Script;

public class BashDebuggerDebugPoint {

  /**
   * Debug mode constants
   **/
  private static final boolean DEBUG_MODE = true;
  // Folder of scripts prepared for debug mode
  private static final String SCRIPTS_PATH = "debug/scripts/";

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

  public static void main(String[] args) {
    final String scriptPath = Objects.requireNonNull(
        ClassLoader
            .getSystemClassLoader()
            .getResource(DebugScripts.SCRIPT_WITH_READING.getScriptFullName())
    ).getPath();

    final Script script = new Script.ScriptBuilder()
        .withPath(scriptPath)
        .readContent()
        .handleContent()
        .build();

    final ScriptExecutor bashScriptExecutor = new ScriptExecutorImpl();

    bashScriptExecutor.execute(script);

  }
}
