package com.dawandeapp.pillreminder.data.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.util.SparseArray;

import com.dawandeapp.pillreminder.data.model.BleDevice;
import com.dawandeapp.pillreminder.utilities.M;

import java.util.Collections;
import java.util.List;
import java.util.Observer;

public class BleExplorer {
    private static volatile BleExplorer instance;
    public static final int SCAN_PERIOD = 10000;    // 10 sec
    public static final int SEARCHING = 1;
    public static final int FREE = 2;
    private final Object lock = new Object();


    public SparseArray<BluetoothDevice> foundDevices;
    volatile int state = FREE;
    private Thread timerThread;
    private BluetoothLeScanner scanner;
    private ScanCallback scanCallback;
    private NewDeviceObserver newDeviceObserver;

    public static BleExplorer getInstance(BluetoothAdapter adapter) {
        if (instance == null || adapter != null) {
            synchronized (BleExplorer.class) {
                if (instance == null || adapter != null) {
                    instance = new BleExplorer(adapter);
                }
            }
        }
        return instance;
    }


    private BleExplorer(BluetoothAdapter adapter) {
        scanner = adapter.getBluetoothLeScanner();
        foundDevices = new SparseArray<>();
    }

    public void startSearching(NewDeviceObserver newDeviceObserver) {
        if (state == FREE) {
            this.newDeviceObserver = newDeviceObserver;
            startTimer();
            List<ScanFilter> noFilter = Collections.emptyList();
            ScanSettings scanSettings = new ScanSettings.Builder()
                    .setReportDelay(10)  // Чтобы не использовать synchronized
                    .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                    .build();
            scanner.startScan(noFilter, scanSettings, getScanCallback());
            M.d("Scanning started successfully");
            state = SEARCHING;
        }
    }

    public void stopSearching() {
        if (state == SEARCHING) {
            synchronized (lock) {   // Если пользователь и таймер сработают одновременно
                if (state == SEARCHING) {
                    scanner.stopScan(getScanCallback());
                    foundDevices.clear();
                    recycle();
                    state = FREE;
                }
            }
        }
    }

    private ScanCallback getScanCallback() {
        if (scanCallback == null) {
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    BluetoothDevice foundDevice = result.getDevice();
                    int hash = foundDevice.hashCode();
                    if (foundDevices.get(hash) == null) {
                        foundDevices.append(hash, foundDevice);
                        newDeviceObserver.update(new BleDevice(foundDevice));
                    } else M.d("The same device was found");
                }

                @Override
                public void onScanFailed(int errorCode) {
                    M.d("Scan failed (");
                }
            };
        }
        return scanCallback;
    }

    public void recycle() {
        newDeviceObserver = null;
        foundDevices.clear();
    }

    private void startTimer() {
        timerThread = new Thread(() -> {
            try {
                Thread.sleep(SCAN_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                BleExplorer.getInstance(null).stopSearching();
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    interface NewDeviceObserver {
        void update(BleDevice newDevice) throws NullPointerException;
    }
}
