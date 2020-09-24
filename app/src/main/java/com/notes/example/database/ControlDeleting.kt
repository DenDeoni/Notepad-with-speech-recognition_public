package com.notes.example.database

import android.app.Activity
import android.content.Context
import com.notes.example.R
import com.notes.example.alerts.ToastAlert


class ControlDeleting(val context: Context,
                      val title: String
) {

    private fun message(): String {
        return if (title.isNotEmpty()) {
            context.getString(R.string.note) + " \"" + title + "\" \n" + context.getString(R.string.note_was_del)
        } else {
            context.getString(R.string.note) + " " + context.getString(R.string.note_was_del)
        }

    }

    //private val messageFalls = context.getString(R.string.note_number) + " \"" + title + "\" \n" + context.getString(R.string.note_was_not_del)

    fun controlDeleting() {
        ToastAlert(context, message()).toastAlert()
    }
}