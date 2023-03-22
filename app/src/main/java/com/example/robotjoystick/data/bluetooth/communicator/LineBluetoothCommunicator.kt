package com.example.robotjoystick.data.bluetooth.communicator

import com.example.robotjoystick.data.bluetooth.communicator.factory.BluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.connector.BluetoothConnector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintStream

class LineBluetoothCommunicator(
    input: InputStream,
    output: OutputStream
) : BluetoothCommunicator<String, String> {

    private val outputStream = PrintStream(output)
    private val reader = BufferedReader(InputStreamReader(input))

    override suspend fun receive(): String = withContext(Dispatchers.IO) {
        reader.readLine()
    }

    override suspend fun send(out: String) = withContext(Dispatchers.IO) {
        outputStream.println(out)
    }

    object Factory : BluetoothCommunicatorFactory<String, String, LineBluetoothCommunicator> {
        override fun create(connector: BluetoothConnector): LineBluetoothCommunicator {
            return LineBluetoothCommunicator(connector.inputStream, connector.outputStream)
        }

    }
}