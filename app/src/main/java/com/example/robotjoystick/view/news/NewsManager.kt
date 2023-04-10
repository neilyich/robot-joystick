package com.example.robotjoystick.view.news

import android.content.Context
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class NewsManager @Inject constructor(
    //private val context: Context,
) {
//    fun show(news: AppNews) {
//        when (news) {
//            is AppNews.Dialog -> show(news)
//        }
//    }

    fun show(dialog: AppNews.Dialog, context: Context) {
        MaterialAlertDialogBuilder(context)
            .setMessage(dialog.title)
            .setPositiveButton(dialog.positiveText, dialog.positiveCallback)
            .setNegativeButton(dialog.negativeText, dialog.negativeCallback)
            .show()
    }

    fun show(toast: AppNews.Toast, context: Context) {
        Toast.makeText(context, toast.text, toast.duration).show()
    }
}