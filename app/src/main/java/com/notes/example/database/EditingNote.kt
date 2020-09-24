package com.notes.example.database

import android.app.Application
import android.content.Context

class EditingNote(val context: Context) {
    private val appDatabase = AppDatabase.getInstance(Application())
    fun editNote(contentPOJO: List<ContentPOJO>) {
        appDatabase.noteContentDao().updateNote(contentPOJO)
        DBChange.DBChange.dbWasChanged = true // todo
    }
}