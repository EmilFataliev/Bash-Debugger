package ru.emil.bashdb.env;

import java.io.IOException;
import ru.emil.bashdb.BashScriptExecutor;
import ru.emil.bashdb.script.entity.Script;

public final class BashEnv {

  // TODO: Should be determined in runtime
  private static final String DEFAULT_BASH_ENVIRONMENT = "/usr/local/bin/bash";

  private static final String BASH_ENV_SCRIPT =
      "#!/usr/bin/env bash"
          + System.lineSeparator()
          + "printenv";

  private static String getDefaultBashEnvironment() {
    return DEFAULT_BASH_ENVIRONMENT;
  }

  public static String getBashEnvironmentRuntime() throws IOException {
    final Script script = new Script.ScriptBuilder()
        .withContent(BASH_ENV_SCRIPT)
        .build();

    final BashScriptExecutor bashScriptExecutor = new BashScriptExecutor(script);

    // TODO: parse bash path from here
    final String env = bashScriptExecutor.run(false);

    return getDefaultBashEnvironment();
  }
}
