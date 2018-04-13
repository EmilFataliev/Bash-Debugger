package com.bash.debugger.script.handling.impl;

import com.bash.debugger.script.handling.api.ScriptHandler;

public final class ScriptHandlerImpl implements ScriptHandler {

  private static final String SHEBANG_LINE = "#!/usr/bin/env bash";
  private static final String SHEBANG_BEGINNING = "#!/";

  private static final String TRACING_ON = "__debug_ON__";

  @Override
  public String handleScript(final String content) {
    final String contentWithShebang = addShebangLine(content);
    return addTracing(contentWithShebang);
  }

  private String addShebangLine(final String content) {

    if (content.startsWith(SHEBANG_LINE)) {
      return content;
    }

    if (content.startsWith(SHEBANG_BEGINNING)) {
      final int shebangIndex = content.indexOf(SHEBANG_BEGINNING);
      final String shebang = content
          .substring(shebangIndex, content.indexOf(System.lineSeparator(), shebangIndex));

      return content.replace(shebang, SHEBANG_LINE);
    }

    return SHEBANG_LINE
        + System.lineSeparator()
        + content;
  }

  private String addTracing(final String content) {

    if (content.contains(TRACING_ON)) {
      return content;
    }

    return new StringBuilder(content)
        .insert(
            SHEBANG_LINE.length() + 1,
            TRACING_ON
                + System.lineSeparator()
        )
        .toString();
  }

}
