package com.example.robotjoystick.data.bluetooth

import java.io.IOException

class BluetoothCommunicationException(
    override val message: String?,
    override val cause: Throwable? = null
) : IOException(message, cause) {
}