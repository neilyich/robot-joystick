package com.example.robotjoystick.domain.bluetooth

import android.bluetooth.BluetoothDevice
import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.data.bluetooth.device.BondState
import javax.inject.Inject

class GetBluetoothDeviceDataUseCase @Inject constructor() {
    @Throws(SecurityException::class)
    operator fun invoke(bluetoothDevice: BluetoothDevice): BluetoothDeviceData {
        return BluetoothDeviceData(
            name = bluetoothDevice.name ?: "",
            address = bluetoothDevice.address ?: "",
            deviceClass = bluetoothDevice.bluetoothClass.majorDeviceClass,
            bondState = BondState.fromState(bluetoothDevice.bondState)
        )
    }
}