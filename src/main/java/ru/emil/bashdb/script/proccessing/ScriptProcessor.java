package ru.emil.bashdb.script.proccessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ScriptProcessor {

  String HEADER = "#!/usr/bin/env bash";

  default String readScript(final Path scriptPath) throws IOException {
    return Files.lines(scriptPath)
        .reduce((acc, line) -> acc + line + "\n")
        .map(content -> {
          if (!content.startsWith(HEADER)) {
            content = HEADER + content;
          }

          return content.substring(0, content.length() - 1);
        })
        .orElseThrow(IllegalArgumentException::new);
  }

  String addTracing(final String scriptLines);

}
