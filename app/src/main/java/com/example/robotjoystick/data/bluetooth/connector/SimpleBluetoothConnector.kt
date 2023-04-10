package com.example.robotjoystick.data.bluetooth.connector

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.robotjoystick.data.bluetooth.BluetoothCommunicationException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@SuppressLint("MissingPermission")
class SimpleBluetoothConnector(
    private val device: BluetoothDevice
) : BluetoothConnector {
    private lateinit var sock: BluetoothSocket
    private val uuids =
        listOf(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))

    private var isConnected = false
    override val inputStream: InputStream
        get() = sock.inputStream
    override val outputStream: OutputStream
        get() = sock.outputStream

    override fun isConnected(): Boolean {
        return isConnected
    }

    override suspend fun connect() {
        if (isConnected) {
            return
        }
        var e: IOException? = null
        for (uuid in uuids) {
            try {
                sock = device.createInsecureRfcommSocketToServiceRecord(uuid)
                sock.connect()
                if (sock.inputStream == null || sock.outputStream == null) {
                    continue
                }
                isConnected = true
                Log.i("SimpleBluetoothConnector", "connect result: true, uuid=$uuid, ${sock.inputStream}, ${sock.outputStream}")
                return
            } catch (e1: IOException) {
                e = e1
            }
        }
        Log.i("SimpleBluetoothConnector", "connect result: false")
        throw BluetoothCommunicationException("Unable to connect", e, BluetoothCommunicationException.Type.CONNECT)
    }

    override suspend fun disconnect() {
        if (isConnected) {
            sock.close()
            isConnected = false
        }
    }

}