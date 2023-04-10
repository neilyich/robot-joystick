package com.example.robotjoystick.domain.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.withConnectPermissions
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@SuppressLint("MissingPermission")
class CreateBondUseCase @Inject constructor(
    private val context: Context,
    private val permissionsManager: BluetoothPermissionsManager
) {

    private var receiver: BroadcastReceiver? = null

    suspend operator fun invoke(device: BluetoothDevice): BluetoothDevice {
        receiver?.let {
            context.unregisterReceiver(receiver)
            receiver = null
        }
        val mutex = Mutex(true)
        var result: BluetoothDevice? = null
        return permissionsManager.withConnectPermissions {
            if (!device.createBond()) {
                throw RuntimeException()
            }
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent != null && intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                        val bondedDevice: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        if (bondedDevice != null && bondedDevice.address == device.address) {
                            val newState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE)
                            if (newState == BluetoothDevice.BOND_BONDED) {
                                result = bondedDevice
                                mutex.unlock()
                            }
                            if (newState != BluetoothDevice.BOND_BONDING) {
                                this@CreateBondUseCase.context.unregisterReceiver(this)
                                receiver = null
                            }
                        }
                    }
                }
            }
            context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
            mutex.lock()
            try {
                return@withConnectPermissions result!!
            } finally {
                mutex.unlock()
            }
        }
    }
}