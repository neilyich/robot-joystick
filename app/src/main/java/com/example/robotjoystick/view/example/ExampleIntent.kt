package com.example.robotjoystick.view.example

import com.example.robotjoystick.view.Intent

sealed class ExampleIntent: Intent {
    object Add : ExampleIntent()
    object Sub : ExampleIntent()
    object BluetoothPermitted : ExampleIntent()
    object BluetoothRejected : ExampleIntent()
}
