package com.example.robotjoystick.domain.bluetooth

interface BluetoothCommunicationUseCase<Out, In> {
    suspend fun sendAndReceive(message: Out): In
    suspend fun stop()
}