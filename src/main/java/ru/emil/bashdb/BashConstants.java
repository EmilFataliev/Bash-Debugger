package ru.emil.bashdb;


// TODO: sawn this
public final class BashConstants {
  public static final String INITIAL_ENV_STATE_FILE_NAME = "init_env_state";
  public static final String RUNTIME_ENV_STATE_FILE_NAME = "run_env_state";

  public static final String BREAK_POINT = "br#";
  public static final String READ_EMPTY = "read";

  public static final String ENV = "(set -o posix; set)";

  public static final String HEADER = "#!/usr/bin/env bash";

  public static final String OVER_WRITE_FILE = " > ";
}
