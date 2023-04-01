package com.example.robotjoystick.data.bluetooth.connector

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@SuppressLint("MissingPermission")
class SimpleBluetoothConnector(
    device: BluetoothDevice
) : BluetoothConnector {
    private val sock: BluetoothSocket

    init {
        val uuid = if (device.uuids.isEmpty()) {
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // SPP uuid
        } else {
            device.uuids[0].uuid
        }
        sock = device.createRfcommSocketToServiceRecord(uuid)
            ?: throw IOException("Unable to create socket")
    }

    private var isConnected = false
    override val inputStream: InputStream = sock.inputStream
    override val outputStream: OutputStream = sock.outputStream

    override fun isConnected(): Boolean {
        return isConnected
    }

    override suspend fun connect() {
        if (!isConnected) {
            sock.connect()
            isConnected = true
        }
    }

    override suspend fun disconnect() {
        if (isConnected) {
            sock.close()
            isConnected = false
        }
    }

}