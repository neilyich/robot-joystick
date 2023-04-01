package com.example.robotjoystick.view.knowndevices

import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.view.State

data class KnownDevicesState(
    val isLoading: Boolean = true,
    val devices: List<BluetoothDeviceData> = emptyList(),
) : State {

    sealed interface Destination {
        data class Joystick(
            val device: BluetoothDeviceData
        ) : Destination

        object ScanNewDevices : Destination
    }

    fun ready() = copy(isLoading = false)
    fun loading() = copy(isLoading = true)
}
