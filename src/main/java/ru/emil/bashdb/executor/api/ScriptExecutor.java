package ru.emil.bashdb.executor.api;

import ru.emil.bashdb.script.entity.Script;

public interface ScriptExecutor {

  void execute(final Script script);
}
