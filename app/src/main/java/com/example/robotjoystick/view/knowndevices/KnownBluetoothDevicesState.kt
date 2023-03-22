package com.example.robotjoystick.view.knowndevices

import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDeviceData
import com.example.robotjoystick.view.State

data class KnownBluetoothDevicesState(
    val isLoading: Boolean = true,
    val devices: List<BluetoothDeviceData> = emptyList()
) : State {
    fun ready() = copy(isLoading = false)
    fun loading() = copy(isLoading = true)
}
