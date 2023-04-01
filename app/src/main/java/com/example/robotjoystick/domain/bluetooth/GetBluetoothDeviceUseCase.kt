package com.example.robotjoystick.domain.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import javax.inject.Inject

class GetBluetoothDeviceUseCase @Inject constructor(
    private val adapter: BluetoothAdapter
) {
    operator fun invoke(bluetoothDeviceData: BluetoothDeviceData): BluetoothDevice {
        return adapter.getRemoteDevice(bluetoothDeviceData.address)
    }
}