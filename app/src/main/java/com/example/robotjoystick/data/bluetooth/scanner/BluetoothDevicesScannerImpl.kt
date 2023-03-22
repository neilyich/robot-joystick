package com.example.robotjoystick.data.bluetooth.scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import javax.inject.Inject

class BluetoothDevicesScannerImpl @Inject constructor(
    private val adapter: BluetoothAdapter,
    private val context: Context
) : BluetoothDevicesScanner {

    private var receiver: FoundBluetoothDeviceReceiver? = null

    @Throws(SecurityException::class)
    override suspend fun pairedDevices(): Set<BluetoothDevice> {
        return adapter.bondedDevices
    }

    @Throws(SecurityException::class)
    override suspend fun scanNewDevices(handler: suspend (found: BluetoothDevice) -> Unit): Boolean {
        if (!adapter.startDiscovery()) return false
        receiver = FoundBluetoothDeviceReceiver(handler)
        val i = context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        Log.i("REG", "$i")
        return true
    }

    @Throws(SecurityException::class)
    override suspend fun stopScan() {
        adapter.cancelDiscovery()
        receiver?.let { context.unregisterReceiver(it) }
    }
}