package ru.emil.bashdb.script.handling.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ScriptHandler {

  /**
   * Simple function for reading script content
   * @param scriptPath path to script
   * @return content of script file
   * @throws IOException if scriptPath not found
   */
  default String readScript(final Path scriptPath) throws IOException {
    return Files.lines(scriptPath)
        .reduce((acc, line) -> acc + line + System.lineSeparator())
        .orElseThrow(IllegalArgumentException::new);
  }

  /**
   * Function for adding header and (set -o posix; set) to the beginning of script (for getting
   * initial environment, p.s. for debugging)
   * @param scriptContent content of script
   * @return normalised content of script
   */
  String normalise(final String scriptContent);

  /**
   * Function for adding to script tracing of execution
   * @param scriptLines normalised script
   * @return traced and normalised content of script
   */
  String addTracing(final String scriptLines);

}
