package com.notes.example.database

import android.app.Application
import android.content.Context

class WritingNoteToDB(val context: Context) {
    private val appDatabase = AppDatabase.getInstance(Application())
    fun writeNoteToDB(contentPOJO: List<ContentPOJO>) {
        appDatabase.noteContentDao().insertNote(contentPOJO)
        DBChange.DBChange.dbWasChanged = true // todo
    }
}