package com.dawandeapp.pillreminder.data.bluetooth;

public class BleManager {
    private static volatile BleManager instance;

    public static BleManager getInstance() {
        if (instance == null) {
            synchronized (BleManager.class) {
                if (instance == null) {
                    instance = new BleManager();
                }
            }
        }
        return instance;
    }

    private BleManager() {

    }

}
