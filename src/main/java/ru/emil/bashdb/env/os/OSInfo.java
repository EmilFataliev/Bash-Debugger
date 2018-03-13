package ru.emil.bashdb.env.os;

import com.google.common.base.Strings;
import java.util.Locale;
import ru.emil.bashdb.env.BashEnvLocationDetection;

/**
 * Enum, which used for detection operating system
 *
 * @see BashEnvLocationDetection
 *
 * Note: I know that Apache SystemUtils have such functional, but it seems to me that it's easier
 * than pulling an extra dependency
 */
public enum OSInfo {
  WINDOWS,
  UNIX,
  MAC,
  OTHER;

  private String OSVersion;
  private static final OSInfo osInfo = detectOS();

  /**
   * Detecting operating system
   *
   * @return operating system name
   */
  private static OSInfo detectOS() {
    final String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
    final String osVersion = System.getProperty("os.version");

    OSInfo tempOsInfo = OTHER;
    tempOsInfo.setOSVersion(osVersion);

    if (Strings.isNullOrEmpty(osName)) {
      throw new UnknownError("Operating system name not found");
    }

    if (Strings.isNullOrEmpty(osVersion)) {
      throw new UnknownError("Operating system version not found");
    }

    if (osName.contains("win")) {
      tempOsInfo = WINDOWS;
    }

    if (osName.contains("nux")
        || osName.contains("nix")
        || osName.contains("aix")
        || osName.contains("freebsd")) {
      tempOsInfo = UNIX;
    }

    if (osName.contains("mac")) {
      tempOsInfo = MAC;
    }

    return tempOsInfo;
  }

  public boolean isWindows() {
    return osInfo.name().equals(WINDOWS.name());
  }

  public boolean isUnix() {
    return osInfo.name().equals(UNIX.name());
  }

  public boolean isMac() {
    return osInfo.name().equals(MAC.name());
  }

  public String getOSVersion() {
    return OSVersion;
  }

  public void setOSVersion(String OSVersion) {
    this.OSVersion = OSVersion;
  }

  public static OSInfo getOsInfo() {
    return osInfo;
  }
}