package com.example.robotjoystick.domain.bluetooth

import com.example.robotjoystick.data.bluetooth.RecoverableBluetoothCommunication
import com.example.robotjoystick.data.bluetooth.communicator.BluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.factory.BluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.withConnectPermissions
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDeviceData
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothCommunicationUseCase @Inject constructor(
    private val connectBluetooth: ConnectBluetoothUseCase,
    private val bluetoothCommunicatorFactory: BluetoothCommunicatorFactory<String, String, out BluetoothCommunicator<String, String>>,
    private val permissionsManager: BluetoothPermissionsManager
) {

    private var communication: RecoverableBluetoothCommunication<String, String, out BluetoothCommunicator<String, String>>? = null

    @Throws(SecurityException::class)
    suspend fun start(bluetoothDeviceData: BluetoothDeviceData) {
        cancel()
        val connector = connectBluetooth(bluetoothDeviceData)
        communication = permissionsManager.withConnectPermissions {
            RecoverableBluetoothCommunication(connector, bluetoothCommunicatorFactory).also { it.start() }
        }
    }

    suspend fun sendAndReceive(message: String): String {
        return communication?.sendAndReceive(message) ?: throw IllegalStateException("Call start() before sendAndReceive()")
    }

    suspend fun cancel() {
        communication?.stop()
        communication = null
    }
}