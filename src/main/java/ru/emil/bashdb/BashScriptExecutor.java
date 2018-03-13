package ru.emil.bashdb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.emil.bashdb.commands.UserInterfaceCommand;
import ru.emil.bashdb.env.BashUserSystemInfo;
import ru.emil.bashdb.script.entity.Script;

// TODO: full refactoring
public class BashScriptExecutor {

  // Exclude from vars set
  private static final String REPLY = "REPLY=";

  private static final Logger logger = LoggerFactory.getLogger(BashScriptExecutor.class);
  private final Script script;

  /* package private */ BashScriptExecutor(final Script script) {
    this.script = script;
  }

  public String run(boolean redirectToConsole) throws IOException {
    final StringBuilder scriptOutput = new StringBuilder();
    final BashUserSystemInfo bashUserSystemInfo = BashUserSystemInfo.INSTANCE.getInstance();
    final ProcessBuilder processBuilder = new ProcessBuilder(
        bashUserSystemInfo.getBashEnvLocation(), "-c",
        script.getTracedContent());

    if (redirectToConsole) {
      processBuilder
          .redirectErrorStream(true)
          .redirectOutput(Redirect.INHERIT);
    }

    final Process process = processBuilder.start();

    try (final BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(process.getInputStream()))) {
      scriptOutput.append(bufferedReader.readLine());
    }

    return scriptOutput.toString();
  }

  public void debug() throws IOException {
    System.out.println(
        String.format(
            "Executing script %s in path %s:",
            script.getPath().toFile().getName(),
            script.getPath().toFile().getParent()
        )
    );

    final BashUserSystemInfo bashUserSystemInfo = BashUserSystemInfo.INSTANCE.getInstance();

    final Process process = new ProcessBuilder(bashUserSystemInfo.getBashEnvLocation(), "-c",
        script.getTracedContent())
        .redirectErrorStream(true)
        .redirectOutput(Redirect.INHERIT)
        .start();

    final Scanner scanner = new Scanner(System.in);

    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(process.getOutputStream()))) {
      while (process.isAlive()) {
        final String userCommand = scanner.next();

        if (userCommand.equals(UserInterfaceCommand.VARIABLES.getCommand())) {
          Set<String> initial =
              Files.lines(Paths.get(BashConstants.INITIAL_ENV_STATE_FILE_NAME))
                  .collect(Collectors.toSet());

          Set<String> runtime =
              Files.lines(Paths.get(BashConstants.RUNTIME_ENV_STATE_FILE_NAME))
                  .collect(Collectors.toSet());

          runtime.removeAll(initial);
          runtime.removeIf(var -> var.startsWith(REPLY));

          runtime.forEach(System.out::println);
        } else if (userCommand.equals(UserInterfaceCommand.RUN.getCommand())) {
          writer.write(System.lineSeparator());
          writer.flush();
        } else if (userCommand.equals(UserInterfaceCommand.HELP.getCommand())) {
          System.out.println(UserInterfaceCommand.getHelp());
        } else if (userCommand.equals(UserInterfaceCommand.STOP.getCommand())) {
          writer.close();
          process.destroy();
          break;
        } else {
          writer.write(userCommand + System.lineSeparator());
          writer.flush();
        }
      }

    } catch (Exception e) {
      logger.error("failed execute " + script.getPath() + " ", e);
    } finally {
      process.destroy();
    }


  }

}
