package com.example.robotjoystick.data.bluetooth.connector.factory

import android.bluetooth.BluetoothDevice
import com.example.robotjoystick.data.bluetooth.connector.BluetoothConnector

interface BluetoothConnectorFactory {
    fun create(device: BluetoothDevice): BluetoothConnector
}