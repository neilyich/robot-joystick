package com.example.robotjoystick.view.scandevices

import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.view.Intent

sealed interface ScanDevicesIntent : Intent {
    data class DeviceClicked(
        val device: BluetoothDeviceData
    ) : ScanDevicesIntent

    object Resumed : ScanDevicesIntent

    object Paused : ScanDevicesIntent

    object BackButtonClicked : ScanDevicesIntent
}