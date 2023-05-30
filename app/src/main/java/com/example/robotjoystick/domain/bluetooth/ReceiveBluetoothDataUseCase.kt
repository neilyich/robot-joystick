package com.example.robotjoystick.domain.bluetooth

import com.example.robotjoystick.data.bluetooth.BluetoothReceiver
import com.example.robotjoystick.data.bluetooth.communicator.ReceiveBluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.withConnectPermissions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiveBluetoothDataUseCase @Inject constructor(
    private val connectBluetooth: ConnectBluetoothUseCase,
    private val permissionsManager: BluetoothPermissionsManager
) {

    private lateinit var communication: BluetoothReceiver

    // подключиться к блютуз-устройству
    @Throws(SecurityException::class)
    suspend fun start(bluetoothDeviceData: BluetoothDeviceData) {
        val connector = connectBluetooth(bluetoothDeviceData)
        communication = permissionsManager.withConnectPermissions {
            BluetoothReceiver(connector) {
                ReceiveBluetoothCommunicator(it.inputStream)
            }.also { it.start() }
        }
    }

    suspend fun subscribe(onReceivedData: (String) -> Unit) {
        communication.subscribe(onReceivedData)
    }

    // отключиться от устройства
    suspend fun stop() {
        communication.stop()
    }

}