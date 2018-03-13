package ru.emil.bashdb.script.handling.impl;

import ru.emil.bashdb.BashConstants;
import ru.emil.bashdb.script.handling.api.ScriptHandler;

public final class ScriptHandlerImpl implements ScriptHandler {

  @Override
  public String normalise(final String scriptContent) {
    final String normalisedScriptContent;

    if (!scriptContent.startsWith(BashConstants.HEADER)) {
      normalisedScriptContent =
          BashConstants.HEADER
              + System.lineSeparator()
              + BashConstants.ENV
              + BashConstants.OVER_WRITE_FILE
              + BashConstants.INITIAL_ENV_STATE_FILE_NAME
              + System.lineSeparator()
              + scriptContent;
    } else {
      int indexAfterHeader = scriptContent.indexOf(System.lineSeparator());
      normalisedScriptContent =
          scriptContent.substring(0, indexAfterHeader + 1)
              + BashConstants.ENV
              + BashConstants.OVER_WRITE_FILE
              + BashConstants.INITIAL_ENV_STATE_FILE_NAME
              + System.lineSeparator()
              + scriptContent.substring(indexAfterHeader, scriptContent.length() - 1);
    }

    return normalisedScriptContent.substring(0, normalisedScriptContent.length() - 1);
  }

  @Override
  public String addTracing(final String scriptLines) {

    final String[] scriptLinesSplited = scriptLines.split("\n");
    boolean isBreakPointLine = false;

    for (int i = 2; i < scriptLinesSplited.length; ++i) {

      // if break point
      if (scriptLinesSplited[i].startsWith(BashConstants.BREAK_POINT)) {
        isBreakPointLine = true;
        scriptLinesSplited[i] = scriptLinesSplited[i].replace(
            BashConstants.BREAK_POINT,
            String.format("%s && %s && ",
                BashConstants.ENV
                    + BashConstants.OVER_WRITE_FILE
                    + BashConstants.RUNTIME_ENV_STATE_FILE_NAME,
                BashConstants.READ_EMPTY)
        );

      }

      // If not comment line or header
      if (!scriptLinesSplited[i].startsWith("#")) {
        String header = "echo \"Line %s: +%s : \" \n %s";
        String tracedLine = scriptLinesSplited[i];

        final String toFind = BashConstants.READ_EMPTY + " && ";

        if (isBreakPointLine) {
          tracedLine = scriptLinesSplited[i].substring(
              scriptLinesSplited[i].indexOf(toFind) + toFind.length() + 1
          );

          header = "echo \"Line %s (break point line): +%s : \" \n %s";
        }

        scriptLinesSplited[i] = String
            .format(
                header,
                i - 1,
                tracedLine,
                scriptLinesSplited[i]
            );
      }

      isBreakPointLine = false;
    }

    return String.join("\n", scriptLinesSplited);
  }
}
