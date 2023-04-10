package com.example.robotjoystick.data.bluetooth.connector

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@SuppressLint("MissingPermission")
class AcceptorBluetoothConnector(
    adapter: BluetoothAdapter,
    name: String,
) : BluetoothConnector {
    private val serverSock = adapter.listenUsingRfcommWithServiceRecord(name, UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))

    private lateinit var sock: BluetoothSocket
    private var isConnected = false

    override val inputStream: InputStream
        get() = sock.inputStream
    override val outputStream: OutputStream
        get() = sock.outputStream
    val connectedDevice: BluetoothDevice get() = sock.remoteDevice

    override fun isConnected(): Boolean {
        return isConnected
    }

    override suspend fun connect() {
        if (isConnected) {
            return
        }
        sock = serverSock.accept()
        isConnected = true
    }

    override suspend fun disconnect() {
        if (isConnected) {
            sock.close()
            isConnected = false
        }
    }
}