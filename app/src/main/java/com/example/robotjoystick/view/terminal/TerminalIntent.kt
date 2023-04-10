package com.example.robotjoystick.view.terminal

import com.example.robotjoystick.view.Intent

sealed interface TerminalIntent : Intent {
    data class SendButtonClicked(
        val text: String
    ) : TerminalIntent

    object ViewCreated: TerminalIntent

    object BackPressed : TerminalIntent

    object DisconnectConfirmed : TerminalIntent

    object Stopped : TerminalIntent

    object NewsShown : TerminalIntent
}