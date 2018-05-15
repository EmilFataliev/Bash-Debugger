package com.bash.debugger.env;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class BashDebuggerUserSystemInfo {

  private static BashDebuggerUserSystemInfo INSTANCE;

  private final OS os;
  private Path bashEnvLocation;

  private static final Collection<String> POSSIBLE_LOCATIONS =
      Collections.unmodifiableList(
          Lists.newArrayList(
              "/sbin/bash",
              "/bin/bash",
              "/usr/bin/bash",
              "/usr/local/bin/bash",
              "/opt/local/bin/bash",
              "/opt/bin/bash",
              "/sbin/sh",
              "/bin/sh",
              "/usr/bin/sh",
              "/opt/local/bin/sh",
              "/opt/bin/sh",
              "/usr/bin/env"
          )
      );

  private static final Collection<String> POSSIBLE_LOCATIONS_WINDOWS =
      Collections.unmodifiableList(
          Lists.newArrayList(
              "c:\\cygwin\\bin\\bash.exe",
              "d:\\cygwin\\bin\\bash.exe",
              "c:\\Program Files\\Git\\usr\\bin\\bash.exe",
              "c:\\Program Files (x86)\\Git\\usr\\bin\\bash.exe"
          )
      );

  private BashDebuggerUserSystemInfo() {
    this.os = detectOS();
    this.bashEnvLocation = detectBashLocation();
  }

  private Path detectBashLocation() {
    final Collection<String> locations = os.isWindows()
        ? POSSIBLE_LOCATIONS_WINDOWS
        : POSSIBLE_LOCATIONS;

    for (final String prospectiveLocation : locations) {
      if (isSuitable(Paths.get(prospectiveLocation).toFile())) {
        return Paths.get(prospectiveLocation);
      }
    }

    // if OS windows and location not found
    if (os.isWindows()) {
      for (final String prospectiveLocation : POSSIBLE_LOCATIONS) {
        if (isSuitable(Paths.get(prospectiveLocation + ".exe").toFile())) {
          return Paths.get(prospectiveLocation);
        }
      }
    }

    throw new IllegalStateException(
        "Could not determine bash location, please use -b option, for init bash location");
  }

  private boolean isSuitable(final File prospectiveLocation) {
    return prospectiveLocation.isFile()
        && prospectiveLocation.canRead()
        && prospectiveLocation.canExecute();
  }

  /**
   * Detecting operating system
   *
   * @return {@code OS}
   */
  private OS detectOS() {
    final String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

    if (Strings.isNullOrEmpty(osName)) {
      throw new UnknownError("Operating system name not found");
    }

    if (osName.contains("nux")
        || osName.contains("nix")
        || osName.contains("aix")
        || osName.contains("freebsd")
        || osName.contains("mac")) {
      return OS.UNIX;
    }

    return OS.WINDOWS;
  }

  public OS getOs() {
    return os;
  }

  public Path getBashEnvLocation() {
    return bashEnvLocation;
  }

  public void setBashEnvLocation(Path bashEnvLocation) {
    this.bashEnvLocation = bashEnvLocation;
  }

  public static BashDebuggerUserSystemInfo getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new BashDebuggerUserSystemInfo();
    }

    return INSTANCE;
  }

  /**
   * Enum, which used for detection operating system
   */
  public enum OS {
    WINDOWS,
    UNIX;

    public boolean isWindows() {
      return this.equals(WINDOWS);
    }
  }
}
