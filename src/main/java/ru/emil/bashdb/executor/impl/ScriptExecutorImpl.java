package ru.emil.bashdb.executor.impl;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.emil.bashdb.env.BashUserSystemInfo;
import ru.emil.bashdb.executor.api.ScriptExecutor;
import ru.emil.bashdb.script.entity.Script;

public class ScriptExecutorImpl implements ScriptExecutor {

  private static final Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);
  private static final String TRACING_SCRIPT_NAME = "tracing/xtrace.sh";

  @Override
  public void execute(final Script script) {
    logger.info(
        String.format(
            "Executing script %s in path %s:",
            script.getPath().toFile().getName(),
            script.getPath().toFile().getParent()
        )
    );

    final BashUserSystemInfo bashUserSystemInfo = BashUserSystemInfo.INSTANCE.getInstance();

    final Path traceScript = Paths.get(
        Objects.requireNonNull(
            getClass()
                .getClassLoader()
                .getResource(TRACING_SCRIPT_NAME)
        ).getPath());

    final ProcessBuilder processBuilder = new ProcessBuilder(
        bashUserSystemInfo.getBashEnvLocation(),
        "-c",
        "sh",
        traceScript.toAbsolutePath().toString(),
        script.getPath().toAbsolutePath().toString()
    )
        .redirectErrorStream(true)
        .redirectInput(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT);

    try {
      processBuilder.start();
    } catch (IOException e) {
      logger.error("Failed executing " + script.getPath() + " ", e);
    }
  }
}
