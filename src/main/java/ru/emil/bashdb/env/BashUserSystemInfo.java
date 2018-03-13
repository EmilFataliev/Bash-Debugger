package ru.emil.bashdb.env;

import ru.emil.bashdb.env.os.OSInfo;

public enum BashUserSystemInfo {
  INSTANCE;

  private final OSInfo osInfo;
  private final String bashEnvLocation;

  private BashUserSystemInfo() {
    this.osInfo = OSInfo.getOsInfo();
    this.bashEnvLocation = BashEnvLocationDetection.detectBashLocation();
  }

  public OSInfo getOsInfo() {
    return osInfo;
  }

  public String getBashEnvLocation() {
    return bashEnvLocation;
  }

  public BashUserSystemInfo getInstance() {
    return INSTANCE;
  }
}
