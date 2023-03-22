package com.example.robotjoystick.view.scandevices

import com.example.robotjoystick.view.State

data class ScanNewBluetoothDevicesState(
    val isLoading: Boolean = true
) : State {
    fun ready() = copy(isLoading = false)
}