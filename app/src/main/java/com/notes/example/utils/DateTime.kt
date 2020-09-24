package com.notes.example.utils

import java.text.SimpleDateFormat
import java.util.*

class DateTime {
    fun getDateTimeString(): String {
        val dateTimeFormat = SimpleDateFormat("d.M.yyyy   HH:mm:ss", Locale.getDefault())
        return dateTimeFormat.format(Date())
    }
}