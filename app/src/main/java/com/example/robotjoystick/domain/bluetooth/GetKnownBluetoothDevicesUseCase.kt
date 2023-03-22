package com.example.robotjoystick.domain.bluetooth

import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.withConnectPermissions
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDeviceData
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDevicesScanner
import javax.inject.Inject

class GetKnownBluetoothDevicesUseCase @Inject constructor(
    private val getBluetoothDeviceData: GetBluetoothDeviceDataUseCase,
    private val devicesScanner: BluetoothDevicesScanner,
    private val permissionsManager: BluetoothPermissionsManager
) {
    suspend operator fun invoke(): List<BluetoothDeviceData> {
        return permissionsManager.withConnectPermissions {
            devicesScanner.pairedDevices().map { getBluetoothDeviceData(it) }.toList()
        }
    }
}