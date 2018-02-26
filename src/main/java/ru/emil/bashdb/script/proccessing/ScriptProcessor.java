package ru.emil.bashdb.script.proccessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public interface ScriptProcessor {

  default String readScript(final Path scriptPath) throws IOException {
    StringBuilder script = new StringBuilder();
    Stream<String> lines = Files.lines(scriptPath);
    lines.forEach(line -> script.append(line).append("\n"));
    script.deleteCharAt(script.length() - 1);
    lines.close();

    return script.toString();
  }

  String addTracing(final String scriptLines);
}
