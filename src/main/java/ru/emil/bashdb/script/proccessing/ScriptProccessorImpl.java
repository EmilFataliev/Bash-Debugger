package ru.emil.bashdb.script.proccessing;

public final class ScriptProccessorImpl implements ScriptProcessor {

  private final String BREAK_POINT = "br#";
  private final String READ_EMPTY = "read && ";

  @Override
  public String addTracing(final String scriptLines) {
    final String[] scriptLinesSplited = scriptLines.split("\n");

    boolean isBreakPointLine = false;

    for (int i = 0; i < scriptLinesSplited.length; ++i) {

      // if break point
      if (scriptLinesSplited[i].startsWith(BREAK_POINT)) {
        isBreakPointLine = true;
        scriptLinesSplited[i] = scriptLinesSplited[i].replace(BREAK_POINT, READ_EMPTY);
      }

      // If not comment line or header
      if (!scriptLinesSplited[i].startsWith("#")) {
        scriptLinesSplited[i] = String
            .format(
                "processenv echo \"Line %s: +%s > \" \n %s",
                i + 1,
                scriptLinesSplited[i]
                    .replace("$", "")
                    .substring(scriptLinesSplited[i].indexOf("&& ")),
                scriptLinesSplited[i]
            );
      }


    }

    return String.join("\n", scriptLinesSplited);
  }
}
