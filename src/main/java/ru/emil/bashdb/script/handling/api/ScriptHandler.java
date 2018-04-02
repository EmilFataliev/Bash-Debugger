package ru.emil.bashdb.script.handling.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ScriptHandler {

  /**
   * Simple function for reading script content
   *
   * @param scriptPath path to script
   * @return content of script file
   * @throws IOException if scriptPath not found
   */
  default String read(final Path scriptPath) throws IOException {
    return Files.lines(scriptPath)
        .reduce((acc, line) -> acc + line + System.lineSeparator())
        .orElseThrow(() -> new IllegalArgumentException(
            "Can't find script file: " + scriptPath.toAbsolutePath().toString()));
  }

  /**
   * Function for handling script content
   * @param content input content of script @link ScriptHandler#read
   * @return handled content of script
   */
  String handleScript(final String content);

}
