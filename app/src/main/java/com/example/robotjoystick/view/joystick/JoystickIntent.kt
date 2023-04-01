package com.example.robotjoystick.view.joystick

import com.example.robotjoystick.view.Intent

sealed interface JoystickIntent : Intent {
    data class ArgumentsReceived(
        val deviceName: String
    ) : JoystickIntent
    data class JoystickDirectionChanged(
        val direction: JoystickDirection
    ) : JoystickIntent

    object BackPressed : JoystickIntent

    object QuitConfirmed : JoystickIntent

    object NewsShown : JoystickIntent
}