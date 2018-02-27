package ru.emil.bashdb.script.proccessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ScriptProcessor {

  default String readScript(final Path scriptPath) throws IOException {
    return Files.lines(scriptPath)
        .reduce((acc, line) -> acc + line + "\n")
        .map(content -> content.substring(0, content.length() - 1))
        .orElseThrow(IllegalArgumentException::new);
  }

  String addTracing(final String scriptLines);
}
