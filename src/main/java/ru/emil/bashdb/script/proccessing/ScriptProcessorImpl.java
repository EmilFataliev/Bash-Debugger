package ru.emil.bashdb.script.proccessing;

import ru.emil.bashdb.BashConstants;

public final class ScriptProcessorImpl implements ScriptProcessor {

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
