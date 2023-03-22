package com.example.robotjoystick.view.scandevices

import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDeviceData
import com.example.robotjoystick.view.Intent

sealed interface ScanNewBluetoothDevicesIntent : Intent {
    data class BluetoothDeviceFound(
        val device: BluetoothDeviceData
    ) : ScanNewBluetoothDevicesIntent
}