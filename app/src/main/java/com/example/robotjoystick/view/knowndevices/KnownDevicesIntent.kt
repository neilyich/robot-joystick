package com.example.robotjoystick.view.knowndevices

import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.view.Intent

sealed interface KnownDevicesIntent : Intent {

    object ScanNewDevicesClicked : KnownDevicesIntent
    object RefreshClicked : KnownDevicesIntent

    object CreateView : KnownDevicesIntent

    data class DeviceClicked(
        val device: BluetoothDeviceData
    ) : KnownDevicesIntent

}