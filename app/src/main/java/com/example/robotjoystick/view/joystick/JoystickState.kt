package com.example.robotjoystick.view.joystick

import androidx.annotation.StringRes
import com.example.robotjoystick.view.State

data class JoystickState(
    val deviceName: String,
    val messages: List<String>,
    val news: News? = null,
) : State {
    sealed interface News {
        data class DisconnectDialog(
            @StringRes
            val title: Int,
            val titleArg: String,
            @StringRes
            val positiveButtonText: Int,
            @StringRes
            val negativeButtonText: Int,
        ) : News

        object PerformHapticFeedback : News
    }
}
