package com.example.robotjoystick.data.bluetooth.communicator.factory

import com.example.robotjoystick.data.bluetooth.communicator.BluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.RetryingBluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.connector.BluetoothConnector

class RetryingBluetoothCommunicatorFactory<In, Out>(
    private val delegateFactory: BluetoothCommunicatorFactory<In, Out, out BluetoothCommunicator<In, Out>>,
    private val maxRetriesCount: Int
) :
    BluetoothCommunicatorFactory<In, Out, RetryingBluetoothCommunicator<In, Out>> {
    override fun create(connector: BluetoothConnector): RetryingBluetoothCommunicator<In, Out> {
        return RetryingBluetoothCommunicator(delegateFactory.create(connector), maxRetriesCount)
    }
}