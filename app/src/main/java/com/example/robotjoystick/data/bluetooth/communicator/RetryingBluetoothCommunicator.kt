package com.example.robotjoystick.data.bluetooth.communicator

import kotlinx.coroutines.delay
import java.io.IOException

class RetryingBluetoothCommunicator<In, Out>(
    private val delegate: BluetoothCommunicator<In, Out>,
    private val maxRetriesCount: Int
) : BluetoothCommunicator<In, Out> {

    override suspend fun receive() = receiveRecoverable()

    override suspend fun send(out: Out) = sendRecoverable(out)

    private suspend fun receiveRecoverable(): In? {
        var readRetries = 0
        var received: In? = receiveOrNull()
        while (received == null) {
            delay(100)
            readRetries++
            if (readRetries >= maxRetriesCount) {
                break
            }
            received = receiveOrNull()
        }
        return received
    }

    private suspend fun receiveOrNull() = try {
        delegate.receive()
    } catch (e: IOException) {
        null
    }

    private suspend fun sendRecoverable(out: Out) {
        var sendRetries = 0
        var sent = sendOrFalse(out)
        while (!sent) {
            delay(100)
            sendRetries++
            if (sendRetries >= maxRetriesCount) {
                break
            }
            sent = sendOrFalse(out)

        }
    }

    private suspend fun sendOrFalse(out: Out) = try {
        delegate.send(out)
        true
    } catch (e: IOException) {
        false
    }

}