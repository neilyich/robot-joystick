package com.example.robotjoystick.data.bluetooth.device

import android.bluetooth.BluetoothDevice

enum class BondState(
    val state: Int
) {
    BONDED(BluetoothDevice.BOND_BONDED),
    BOND_BONDING(BluetoothDevice.BOND_BONDING),
    BOND_NONE(BluetoothDevice.BOND_NONE);

    companion object {
        private val values = values().associateBy { it.state }

        fun fromState(profileState: Int): BondState {
            return values[profileState] ?: throw IllegalArgumentException("Unknown bluetooth state: $profileState")
        }
    }
}
