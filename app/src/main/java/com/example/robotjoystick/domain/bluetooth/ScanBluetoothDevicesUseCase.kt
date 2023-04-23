package com.example.robotjoystick.domain.bluetooth

import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.ignoringPermissions
import com.example.robotjoystick.data.bluetooth.permissions.withScanPermissions
import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDevicesScanner
import javax.inject.Inject

// класс отвечает за составление и обновление списка доступных устройств
class ScanBluetoothDevicesUseCase @Inject constructor(
    private val getBluetoothDeviceData: GetBluetoothDeviceDataUseCase,
    private val devicesScanner: BluetoothDevicesScanner,
    private val permissionsManager: BluetoothPermissionsManager
) {

    private val foundDevices = ArrayList<BluetoothDeviceData>()

    // начать сканирование доступных устройств
    suspend fun start(handler: suspend (updatedFoundDevices: List<BluetoothDeviceData>) -> Unit) {
        stop()
        foundDevices.clear()
        permissionsManager.withScanPermissions {
            devicesScanner.scanDevices { found ->
                val foundDeviceData = getBluetoothDeviceData(found)
                if (foundDeviceData.address.isBlank()) return@scanDevices
                var updated = false
                synchronized(foundDevices) {
                    val index = foundDevices.indexOfFirst { it.address == found.address }
                    if (index < 0) {
                        updated = true
                        foundDevices.add(foundDeviceData)
                    } else {
                        if (foundDeviceData.name.isNotBlank()) {
                            updated = true
                            foundDevices[index] = foundDeviceData
                        }
                        return@synchronized
                    }
                }
                if (updated) {
                    handler(foundDevices.toList())
                }
            }
        }
    }

    // закончить сканирование доступных устройств
    suspend fun stop() = ignoringPermissions { devicesScanner.stopScan() }
}