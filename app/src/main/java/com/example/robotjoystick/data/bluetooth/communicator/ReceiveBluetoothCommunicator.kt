package com.example.robotjoystick.data.bluetooth.communicator

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ReceiveBluetoothCommunicator(
    input: InputStream,
) : BluetoothCommunicator<String, Any?> {

    private val reader = BufferedReader(InputStreamReader(input))

    override suspend fun receive(): String = withContext(Dispatchers.IO) {
        Log.i("RECEIVING", "!!!")
        reader.readLine()
    }

    override suspend fun send(out: Any?) {

    }
}