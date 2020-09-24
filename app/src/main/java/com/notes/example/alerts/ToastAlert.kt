package com.notes.example.alerts

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.core.app.ActivityCompat

class ToastAlert @JvmOverloads constructor(
        private val context: Context,
        private val textMessage: String,
        private val yOffset: Int = 10,
        private val toastBgColor: Int = Color.rgb(255, 111, 0),
        private val toastTextColor: Int = Color.WHITE,
        private val toastTextSize: Float = 18f,
        private val xOffset: Int = 0
) {

    fun toastAlert() {

        val toast = android.widget.Toast.makeText(context, textMessage, android.widget.Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP or Gravity.CENTER, xOffset, yOffset)
        val toastView = toast.view
        toastView.setPadding(10, 10, 10, 10)
        toastView.setBackgroundColor(toastBgColor)

        val textView: TextView = toastView.findViewById(android.R.id.message)
        textView.setPadding(20, 0, 20, 0)
        textView.setTextColor(toastTextColor)

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, toastTextSize)
        toastView.scaleX = 0f
        toastView.scaleY = 0f
        toastView.animate().scaleX(1f).scaleY(1f).setDuration(300).start()

        toast.show()
    }
}


