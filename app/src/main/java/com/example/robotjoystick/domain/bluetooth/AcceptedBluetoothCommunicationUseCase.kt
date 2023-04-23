package com.example.robotjoystick.domain.bluetooth

import android.util.Log
import com.example.robotjoystick.data.bluetooth.BluetoothCommunicationException
import com.example.robotjoystick.data.bluetooth.BluetoothCommunication
import com.example.robotjoystick.data.bluetooth.communicator.BluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.factory.BluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.connector.AcceptorBluetoothConnector
import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.withConnectPermissions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AcceptedBluetoothCommunicationUseCase @Inject constructor(
    private val bluetoothCommunicatorFactory: BluetoothCommunicatorFactory<String, String, out BluetoothCommunicator<String, String>>,
    private val permissionsManager: BluetoothPermissionsManager,
    private val acceptorBluetoothConnector: AcceptorBluetoothConnector,
    private val getBluetoothDeviceData: GetBluetoothDeviceDataUseCase
) : BluetoothCommunicationUseCase<String, String> {

    private var communication: BluetoothCommunication<String, String, out BluetoothCommunicator<String, String>>? = null

    private var _device: BluetoothDeviceData? = null

    val device get() = _device

    suspend fun start(): BluetoothDeviceData {
        stop()
        communication = permissionsManager.withConnectPermissions {
            BluetoothCommunication(acceptorBluetoothConnector, bluetoothCommunicatorFactory).also { it.start() }
        }
        val d = getBluetoothDeviceData(acceptorBluetoothConnector.connectedDevice)
        _device = d
        return d
    }

    override suspend fun sendAndReceive(message: String): String {
        Log.i("TEST1", "sendAndReceive $communication")
        return communication?.sendAndReceive(message) ?:
            throw BluetoothCommunicationException("Call start() before sendAndReceive()", null, BluetoothCommunicationException.Type.SEND)
    }

    override suspend fun stop() {
        communication?.stop()
        communication = null
        _device = null
        Log.i("AcceptedBluetoothCommunicationUseCase", "stopped")
    }

}