package com.bash.debugger;

import com.bash.debugger.executor.api.ScriptExecutor;
import com.bash.debugger.executor.impl.ScriptExecutorImpl;
import com.bash.debugger.script.entity.Script;
import java.io.File;
import java.util.Objects;

/**
 * Class for debugging application
 */
public class BashDebuggerDebugPoint {

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
    final File scriptFile = new File(Objects.requireNonNull(
        ClassLoader
            .getSystemClassLoader()
            .getResource(DebugScripts.MEDIUM_SCRIPT.getScriptFullName())
    ).getPath());

    scriptFile.setExecutable(true);

    final Script script = new Script.ScriptBuilder()
        .withPath(scriptFile.getPath())
        .readContent()
        .handleContent()
        .build();

    final ScriptExecutor bashScriptExecutor = new ScriptExecutorImpl();

    bashScriptExecutor.execute(script);

  }
}
