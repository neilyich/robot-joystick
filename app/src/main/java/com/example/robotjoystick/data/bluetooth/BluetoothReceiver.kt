package com.example.robotjoystick.data.bluetooth

import android.util.Log
import com.example.robotjoystick.data.bluetooth.communicator.ReceiveBluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.factory.BluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.connector.BluetoothConnector

class BluetoothReceiver(
    private val connector: BluetoothConnector,
    private val communicatorFactory: BluetoothCommunicatorFactory<String, Any?, ReceiveBluetoothCommunicator>
) {
    private lateinit var communicator: ReceiveBluetoothCommunicator

    suspend fun start() {
        Log.i("BluetoothReceiver", "connecting")
        connector.connect()
        communicator = communicatorFactory.create(connector)
        Log.i("BluetoothReceiver", "connected")
    }

    suspend fun subscribe(onReceiveData: (String) -> Unit) {
        if (!connector.isConnected()) {
            throw BluetoothCommunicationException("Could not send message (disconnected)", null, BluetoothCommunicationException.Type.SEND)
        }
        Log.i("BluetoothReceiver", "Subscribe")
        while (true) {
            communicator.receive().let {
                Log.i("BluetoothReceiver", "Received $it")
                onReceiveData(it)
            }
        }
    }

    suspend fun stop() {
        connector.disconnect()
    }

}