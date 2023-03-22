package com.example.robotjoystick.data.bluetooth.communicator

interface BluetoothCommunicator<In, Out> {
    suspend fun receive(): In?
    suspend fun send(out: Out)
}