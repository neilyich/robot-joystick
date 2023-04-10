package com.example.robotjoystick.data.bluetooth

import java.io.IOException

class BluetoothCommunicationException(
    override val message: String?,
    override val cause: Throwable? = null,
    val type: Type,
) : IOException(message, cause) {
    enum class Type {
        CONNECT, SEND, RECEIVE
    }
}