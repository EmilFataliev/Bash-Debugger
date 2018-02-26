package ru.emil.bashdb.script.proccessing;

public final class ScriptProccessorImpl implements ScriptProcessor {

  @Override
  public String addTracing(final String scriptLines) {
    final String[] scriptLinesSplitted = scriptLines.split("\n");

    for (int i = 0; i < scriptLinesSplitted.length; ++i) {
      if (!scriptLinesSplitted[i].startsWith("#")) {
        scriptLinesSplitted[i] = String
            .format("echo \"Line %s +%s: \" \n %s", i, scriptLinesSplitted[i], scriptLinesSplitted[i]);
      }
    }

    return String.join("\n", scriptLinesSplitted);
  }
}
