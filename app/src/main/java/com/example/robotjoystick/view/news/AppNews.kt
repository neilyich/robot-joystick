package com.example.robotjoystick.view.news

import android.content.DialogInterface.OnClickListener

sealed interface AppNews {
    data class Dialog(
        val title: String,
        val positiveText: String,
        val negativeText: String,
        val positiveCallback: OnClickListener?,
        val negativeCallback: OnClickListener?,
    ) : AppNews

    data class Toast(
        val text: String,
        val duration: Int = android.widget.Toast.LENGTH_SHORT
    ) : AppNews
}