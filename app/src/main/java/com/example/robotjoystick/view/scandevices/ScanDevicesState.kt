package com.example.robotjoystick.view.scandevices

import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.view.State

data class ScanDevicesState(
    val foundDevices: List<BluetoothDeviceData>
) : State {
    sealed interface Destination {
        object Back : Destination

        data class Joystick(
            val device: BluetoothDeviceData
        ) : Destination
    }
}