package ru.emil.bashdb.env;

import java.io.IOException;
import ru.emil.bashdb.BashScriptExecutor;
import ru.emil.bashdb.script.entity.Script;

public final class BashEnv {
  // TODO: Should be determined in runtime
  private static final String BASH_ENVIRONMENT = "/usr/local/bin/bash";

  private static final String BASH_ENV_SCRIPT = "#!/usr/bin/env bash \n printenv";

  public static String getEnvironment() {
    return BASH_ENVIRONMENT;
  }

  public static String getBashEnvironmentRuntime() throws IOException {
    final Script script = new Script(
        null,
        BASH_ENV_SCRIPT,
        null,
        null
    );

    final BashScriptExecutor bashScriptExecutor = new BashScriptExecutor(script);

    // TODO: parse bash path from here
    final String env = bashScriptExecutor.run(false);


    return "";
  }
}
