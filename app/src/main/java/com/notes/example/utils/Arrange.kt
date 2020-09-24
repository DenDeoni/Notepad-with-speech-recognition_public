package com.notes.example.utils


import android.content.SharedPreferences
import com.notes.example.constans.*

class Arrange (prefs: SharedPreferences) {

    var sortBy: String by SharedPrefs(prefs, SORT_BY, BY_DATE)
    var asc: Boolean by SharedPrefs(prefs, ASC, true)

    fun sortBy(): String {
        return when (sortBy) {
            BY_DATE -> {
                BY_DATE
            }
            BY_CHANGE -> {
                BY_CHANGE
            }
            else -> {
                BY_TITLE
            }
        }
    }

    fun asc(): Boolean {
        return when (asc) {
            true -> {
                true
            }
            else -> {
                false
            }
        }
    }

}