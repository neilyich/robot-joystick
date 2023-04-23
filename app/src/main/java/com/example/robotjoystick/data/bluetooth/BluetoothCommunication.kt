package com.example.robotjoystick.data.bluetooth

import com.example.robotjoystick.data.bluetooth.communicator.BluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.factory.BluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.connector.BluetoothConnector
import java.io.IOException

class BluetoothCommunication<In, Out, Communicator : BluetoothCommunicator<In, Out>>(
    private val connector: BluetoothConnector,
    private val communicatorFactory: BluetoothCommunicatorFactory<In, Out, Communicator>
) {

    private lateinit var communicator: Communicator

    suspend fun start() {
        connector.connect()
        communicator = communicatorFactory.create(connector)
    }

    suspend fun sendAndReceive(o: Out): In {
        if (!connector.isConnected()) {
            throw BluetoothCommunicationException("Could not send message (disconnected)", null, BluetoothCommunicationException.Type.SEND)
        }
        try {
            communicator.send(o)

        } catch (e: IOException) {
            throw BluetoothCommunicationException("Could not send message", e, BluetoothCommunicationException.Type.SEND)
        }
        return communicator.receive() ?:
            throw BluetoothCommunicationException("Could not read answer", null, BluetoothCommunicationException.Type.RECEIVE)
    }

    suspend fun stop() {
        connector.disconnect()
    }
}