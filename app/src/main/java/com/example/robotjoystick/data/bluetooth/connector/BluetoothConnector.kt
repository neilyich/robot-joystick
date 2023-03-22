package com.example.robotjoystick.data.bluetooth.connector

import java.io.InputStream
import java.io.OutputStream

interface BluetoothConnector {
    val inputStream: InputStream
    val outputStream: OutputStream
    fun isConnected(): Boolean
    suspend fun connect()
    suspend fun disconnect()
}