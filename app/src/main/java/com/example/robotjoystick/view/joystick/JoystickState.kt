package com.example.robotjoystick.view.joystick

import com.example.robotjoystick.view.State

data class JoystickState(
    val deviceName: String,
    val messages: List<String>
) : State
