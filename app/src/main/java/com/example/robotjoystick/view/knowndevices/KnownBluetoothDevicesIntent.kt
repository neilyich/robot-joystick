package com.example.robotjoystick.view.knowndevices

import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDeviceData
import com.example.robotjoystick.view.Intent

sealed interface KnownBluetoothDevicesIntent : Intent {

    object ScanNewDevicesClicked : KnownBluetoothDevicesIntent
    object RefreshClicked : KnownBluetoothDevicesIntent

    data class BluetoothDeviceClicked(
        val bluetoothDeviceData: BluetoothDeviceData
    ) : KnownBluetoothDevicesIntent

}