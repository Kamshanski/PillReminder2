package com.dawandeapp.pillreminder.data.model;

import android.bluetooth.BluetoothDevice;

/** Class to keep MAC, name and device hashcode (to let UI find the device quickly) */
public class BleDevice {
    BluetoothDevice device;

    public BleDevice(BluetoothDevice device) {
        this.device = device;
    }

    public String getMAC() {
        return device.getAddress();
    }

    public String getName() {
        String name = device.getName();
        if (name == null || name.isEmpty()) {
            name = "NoName(" + getMAC() + ")";
        }
        return name;
    }

    public int getHash() {
        return device.hashCode();
    }
}
