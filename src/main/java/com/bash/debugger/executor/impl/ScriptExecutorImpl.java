package com.bash.debugger.executor.impl;

import com.bash.debugger.env.BashUserSystemInfo;
import com.bash.debugger.executor.api.ScriptExecutor;
import com.bash.debugger.script.entity.Script;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptExecutorImpl implements ScriptExecutor {

  private static final Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);
  private static final String TRACING_SCRIPT_NAME = "tracing/xtrace.sh";

  @Override
  public void execute(final Script script) {
    logger.debug(
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

    Preconditions.checkState(traceScript.toFile().setExecutable(true));
    Preconditions.checkState(script.getPath().toFile().canExecute());

    // TODO kot 1: текущий вариант
    final ProcessBuilder processBuilder = new ProcessBuilder(
        bashUserSystemInfo.getBashEnvLocation(),
        "-c",
        "sh " +
            traceScript.toAbsolutePath().toString() + " " +
        script.getPath().toAbsolutePath().toString()
    )
        // пробовал играться с редиректами, что-то происходит но не понятно что
        .redirectErrorStream(true)
        .redirectInput(Redirect.INHERIT)
        .redirectOutput(Redirect.INHERIT);

    // по сути пробуем исполнять такую конструкцию
    // /bin/bash -c "sh /Users/emilfataliev/Desktop/bashdb/target/classes/tracing/xtrace.sh /Users/emilfataliev/Desktop/bashdb/src/main/resources/debug/scripts/medium_script.sh"
    try {
      processBuilder.start();
    } catch (IOException e) {
      logger.error("Failed executing " + script.getPath() + " ", e);
    }

    // TODO kot 2: вариант реализации с помощью Runtime executor'a
    /**
     Runtime.getRuntime().exec(new String[]{
     bashUserSystemInfo.getBashEnvLocation() + " -c \"sh "
     + traceScript.toAbsolutePath().toString()
     + " " + script.getPath().toAbsolutePath().toString() + "\""
     });

     **/

    // TODO kot 3: раньше было примерно так
    /**
     try {
     final Process process = new ProcessBuilder(bashUserSystemInfo.getBashEnvLocation(), "-c",
     script.getHandledContent())
     .redirectErrorStream(true)
     .redirectOutput(Redirect.INHERIT)
     .start();

     final BufferedWriter writer = new BufferedWriter(
     new OutputStreamWriter(process.getOutputStream()));

     final Scanner scanner = new Scanner(System.in);

     while (process.isAlive()) {
     final String userCommand = scanner.next();
     writer.write(userCommand + System.lineSeparator());
     writer.flush();
     }
     } catch (IOException e) {
     e.printStackTrace();
     }
     **/
    // TODO kot 4: возможно стоит обратить внимание на <code>Redirect.PIPE</code>
    // Цель запустить трасировку через java

  }
}
