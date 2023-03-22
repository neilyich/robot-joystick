package com.example.robotjoystick.data.bluetooth

import android.util.Log
import com.example.robotjoystick.data.bluetooth.communicator.BluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.factory.BluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.connector.BluetoothConnector
import java.io.IOException

class RecoverableBluetoothCommunication<In, Out, Communicator : BluetoothCommunicator<In, Out>>(
    private val connector: BluetoothConnector,
    private val communicatorFactory: BluetoothCommunicatorFactory<In, Out, Communicator>
) {

    private lateinit var communicator: Communicator

    suspend fun start() {
        connector.connect()
        communicator = communicatorFactory.create(connector)
        Log.i("CONNECTED", "!!!!")
    }

    suspend fun sendAndReceive(o: Out): In {
        try {
            communicator.send(o)

        } catch (e: IOException) {
            throw BluetoothCommunicationException("Could not send message", e)
        }
        return communicator.receive() ?: throw BluetoothCommunicationException("Could not read answer")
    }

    suspend fun stop() {
        connector.disconnect()
    }
}