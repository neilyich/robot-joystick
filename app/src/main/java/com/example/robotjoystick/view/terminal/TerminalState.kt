package com.example.robotjoystick.view.terminal

import androidx.annotation.StringRes
import com.example.robotjoystick.view.State

data class TerminalState(
    val title: String? = null,
    @StringRes val defaultTitleRes: Int,
    val messages: List<Message> = emptyList(),
    val news: News? = null
) : State {
    data class Message(
        val id: Int,
        val from: String,
        val text: String,
    )

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

        data class Toast(
            @StringRes
            val text: Int,
            val arg: String,
        ) : News
    }
}