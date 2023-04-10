package com.example.robotjoystick.domain.bluetooth

import com.example.robotjoystick.data.bluetooth.BluetoothCommunicationException
import com.example.robotjoystick.data.bluetooth.RecoverableBluetoothCommunication
import com.example.robotjoystick.data.bluetooth.communicator.BluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.factory.BluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.withConnectPermissions
import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientBluetoothCommunicationUseCase @Inject constructor(
    private val connectBluetooth: ConnectBluetoothUseCase,
    private val bluetoothCommunicatorFactory: BluetoothCommunicatorFactory<String, String, out BluetoothCommunicator<String, String>>,
    private val permissionsManager: BluetoothPermissionsManager
) : BluetoothCommunicationUseCase<String, String> {

    private var communication: RecoverableBluetoothCommunication<String, String, out BluetoothCommunicator<String, String>>? = null

    @Throws(SecurityException::class)
    suspend fun start(bluetoothDeviceData: BluetoothDeviceData) {
        stop()
        val connector = connectBluetooth(bluetoothDeviceData)
        communication = permissionsManager.withConnectPermissions {
            RecoverableBluetoothCommunication(connector, bluetoothCommunicatorFactory).also { it.start() }
        }
    }

    override suspend fun sendAndReceive(message: String): String {
        return communication?.sendAndReceive(message) ?:
            throw BluetoothCommunicationException("Call start() before sendAndReceive()", null, BluetoothCommunicationException.Type.SEND)
    }

    override suspend fun stop() {
        communication?.stop()
        communication = null
    }
}