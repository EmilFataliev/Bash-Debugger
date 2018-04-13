package com.bash.debugger.script.handling.impl;

import com.bash.debugger.script.handling.api.ScriptHandler;

public final class ScriptHandlerImpl implements ScriptHandler {

  private static final String SHEBANG_LINE = "#!/usr/bin/env bash";
  private static final String TRACING_ON = "__trace_ON__";

  @Override
  public String handleScript(final String content) {
    return addTracing(addShebangLine(content));
  }

  private String addShebangLine(final String content) {
    if (!content.startsWith(SHEBANG_LINE)) {
      return
          SHEBANG_LINE
              + System.lineSeparator()
              + content;
    }

    return content;
  }

  private String addTracing(final String content) {

    if (content.contains(TRACING_ON))
      return content;

    return new StringBuilder(content)
        .insert(
            SHEBANG_LINE.length() + 1,
            TRACING_ON
                + System.lineSeparator()
        )
        .toString();
  }

}
