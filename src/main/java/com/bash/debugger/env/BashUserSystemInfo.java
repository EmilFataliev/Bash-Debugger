package com.bash.debugger.env;

import com.bash.debugger.env.os.OSInfo;

public enum BashUserSystemInfo {
  INSTANCE;

  private final OSInfo osInfo;
  private String bashEnvLocation;

  private BashUserSystemInfo() {
    this.osInfo = OSInfo.getOsInfo();
    this.bashEnvLocation = BashEnvLocationDetection
        .detectBashLocation()
        .orElseThrow(() -> new IllegalStateException("Could not determine bash location"));
  }

  public OSInfo getOsInfo() {
    return osInfo;
  }

  public String getBashEnvLocation() {
    return bashEnvLocation;
  }

  public void setBashEnvLocation(String bashEnvLocation) {
    this.bashEnvLocation = bashEnvLocation;
  }

  public BashUserSystemInfo getInstance() {
    return INSTANCE;
  }
}
