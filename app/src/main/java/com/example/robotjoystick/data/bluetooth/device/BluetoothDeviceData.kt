package com.example.robotjoystick.data.bluetooth.device

data class BluetoothDeviceData(
    val name: String,
    val address: String,
    val deviceClass: Int,
    val bondState: BondState
)
