package com.example.robotjoystick.data.bluetooth.communicator.factory

import com.example.robotjoystick.data.bluetooth.communicator.BluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.connector.BluetoothConnector

@FunctionalInterface
fun interface BluetoothCommunicatorFactory<In, Out, Communicator : BluetoothCommunicator<In, Out>> {
    fun create(connector: BluetoothConnector): Communicator
}