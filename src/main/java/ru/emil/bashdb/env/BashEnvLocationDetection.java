package ru.emil.bashdb.env;

import com.google.common.collect.Lists;
import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import ru.emil.bashdb.env.os.OSInfo;

public final class BashEnvLocationDetection {

  private static final Collection<String> POSSIBLE_LOCATIONS = Collections
      .unmodifiableList(Lists.newArrayList(
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
      ));


  private static final Collection<String> POSSIBLE_LOCATIONS_WINDOWS = Collections
      .unmodifiableList(
          Lists.newArrayList(
              "c:\\cygwin\\bin\\bash.exe",
              "d:\\cygwin\\bin\\bash.exe"
          ));

  private BashEnvLocationDetection() {}

  public static String detectBashLocation() {
    final OSInfo osInfo = OSInfo.getOsInfo();

    final Collection<String> locations = osInfo.isWindows()
        ? POSSIBLE_LOCATIONS_WINDOWS
        : POSSIBLE_LOCATIONS;

    for (final String prospectiveLocation : locations) {
      if (isSuitable(Paths.get(prospectiveLocation).toFile())) {
        return prospectiveLocation;
      }
    }

    // if OS windows and location not find
    if (osInfo.isWindows()) {
      for (final String prospectiveLocation : POSSIBLE_LOCATIONS) {
        if (isSuitable(Paths.get(prospectiveLocation).toFile())) {
          return prospectiveLocation;
        }
      }
    }

    return null;
  }

  private static boolean isSuitable(final File prospectiveLocation) {
    return prospectiveLocation.isFile()
        && prospectiveLocation.canRead()
        && prospectiveLocation.canExecute();
  }
}
