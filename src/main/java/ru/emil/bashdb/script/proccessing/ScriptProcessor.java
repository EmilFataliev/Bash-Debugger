package ru.emil.bashdb.script.proccessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import ru.emil.bashdb.BashConstants;

public interface ScriptProcessor {

  // TODO: нарушение абстракции, это чтение с измененением скрипта (разбить на два иил переименовать)
  default String readScript(final Path scriptPath) throws IOException {
    return Files.lines(scriptPath)
        .reduce((acc, line) -> acc + line + System.lineSeparator())
        .map(content -> {
          if (!content.startsWith(BashConstants.HEADER)) {
            content =
                BashConstants.HEADER
                    + System.lineSeparator()
                    + BashConstants.ENV
                    + BashConstants.OVER_WRITE_FILE
                    + BashConstants.INITIAL_ENV_STATE_FILE_NAME
                    + System.lineSeparator()
                    + content;
          } else {
            int indexAfterHeader = content.indexOf(System.lineSeparator());
            content =
                content.substring(0, indexAfterHeader + 1)
                    + BashConstants.ENV
                    + BashConstants.OVER_WRITE_FILE
                    + BashConstants.INITIAL_ENV_STATE_FILE_NAME
                    + System.lineSeparator()
                    + content.substring(indexAfterHeader, content.length() - 1);
          }

          return content.substring(0, content.length() - 1);
        })
        .orElseThrow(IllegalArgumentException::new);
  }

  String addTracing(final String scriptLines);

}
