package com.example.robotjoystick.data.bluetooth.scanner

import android.bluetooth.BluetoothDevice

interface BluetoothDevicesScanner {
    @Throws(SecurityException::class)
    suspend fun pairedDevices(): Set<BluetoothDevice>
    @Throws(SecurityException::class)
    suspend fun scanDevices(handler: suspend (found: BluetoothDevice) -> Unit)
    suspend fun stopScan()
}