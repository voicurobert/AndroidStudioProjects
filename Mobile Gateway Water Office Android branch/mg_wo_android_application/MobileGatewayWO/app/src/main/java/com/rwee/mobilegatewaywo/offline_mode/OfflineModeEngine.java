package com.rwee.mobilegatewaywo.offline_mode;

/**
 * Created by Intern on 2/25/2016.
 */
public class OfflineModeEngine {

    private boolean offlineMode = false;
    public static OfflineModeEngine instance = new OfflineModeEngine();
    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;
    }
    public boolean isOfflineMode() {
        return offlineMode;
    }


}
