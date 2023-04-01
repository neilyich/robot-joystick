package com.example.robotjoystick.data.bluetooth.scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class BluetoothDevicesScannerImpl @Inject constructor(
    private val adapter: BluetoothAdapter,
    private val context: Context
) : BluetoothDevicesScanner {

    private var receiver: FoundBluetoothDeviceReceiver? = null

    private val isScanning = AtomicBoolean(false)

    @Throws(SecurityException::class)
    override suspend fun pairedDevices(): Set<BluetoothDevice> {
        return adapter.bondedDevices
    }

    @Throws(SecurityException::class)
    override suspend fun scanNewDevices(handler: suspend (found: BluetoothDevice) -> Unit): Boolean {
        stopScan()
        synchronized(isScanning) {
            if (isScanning.get()) return true
            if (!adapter.startDiscovery()) return false
            receiver = FoundBluetoothDeviceReceiver(handler)
            context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
            Log.i("REG", "here")
            isScanning.set(true)
        }
        return true
    }

    @Throws(SecurityException::class)
    override suspend fun stopScan() {
        if (!isScanning.get()) return
        synchronized(isScanning) {
            if (!isScanning.get()) return
            adapter.cancelDiscovery()
            receiver?.let { context.unregisterReceiver(it) }
            isScanning.set(false)
        }
    }
}