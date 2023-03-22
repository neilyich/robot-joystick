package com.example.robotjoystick.domain.bluetooth

import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.ignoringPermissions
import com.example.robotjoystick.data.bluetooth.permissions.withScanPermissions
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDeviceData
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDevicesScanner
import javax.inject.Inject

class ScanBluetoothDevicesUseCase @Inject constructor(
    private val getBluetoothDeviceData: GetBluetoothDeviceDataUseCase,
    private val devicesScanner: BluetoothDevicesScanner,
    private val permissionsManager: BluetoothPermissionsManager
) {

    suspend fun start(handler: suspend (found: BluetoothDeviceData) -> Unit): Boolean {
        return permissionsManager.withScanPermissions {
            devicesScanner.scanNewDevices {
                handler(getBluetoothDeviceData(it))
            }
        }
    }

    suspend fun stop() = ignoringPermissions { devicesScanner.stopScan() }
}