package com.bash.debugger.executor.api;

import com.bash.debugger.script.entity.Script;
import java.io.IOException;

public interface ScriptExecutor {

  void execute(final Script script) throws IOException;
}
