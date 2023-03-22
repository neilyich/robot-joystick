package com.example.robotjoystick.data.bluetooth.scanner

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoundBluetoothDeviceReceiver(
    private val handler: suspend (found: BluetoothDevice) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BluetoothDevice.ACTION_FOUND) {
            Log.i("FOUND", "!!!")
            val found: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            CoroutineScope(Dispatchers.IO).launch {
                found?.let { handler(it) }
            }
        } else {
            Log.i("AAAA", "${intent?.action}")
        }
    }

}