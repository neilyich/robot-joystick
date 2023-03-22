package com.example.robotjoystick.data.bluetooth.connector

import android.bluetooth.BluetoothDevice
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class RetryingBluetoothConnector(
    private val delegate: BluetoothConnector,
    private val maxRetriesCount: Int
) : BluetoothConnector {

    constructor(device: BluetoothDevice, maxRetriesCount: Int) :
            this(SimpleBluetoothConnector(device), maxRetriesCount)

    override val inputStream: InputStream = delegate.inputStream
    override val outputStream: OutputStream = delegate.outputStream

    override fun isConnected(): Boolean {
        return delegate.isConnected()
    }

    override suspend fun connect() {
        var currentRetriesCount = 0
        while (!delegate.isConnected()) {
            try {
                delegate.connect()
            } catch (e: IOException) {
                if (currentRetriesCount >= maxRetriesCount) {
                    throw e
                }
                currentRetriesCount++
            }
        }
    }

    override suspend fun disconnect() {
        delegate.disconnect()
    }

}