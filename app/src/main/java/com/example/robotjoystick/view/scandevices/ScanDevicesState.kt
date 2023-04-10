package com.example.robotjoystick.view.scandevices

import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.view.State

data class ScanDevicesState(
    val foundDevices: List<BluetoothDeviceData>
) : State {
    sealed interface News {

    }
}