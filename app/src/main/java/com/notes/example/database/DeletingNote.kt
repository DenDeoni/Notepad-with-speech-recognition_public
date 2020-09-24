package com.notes.example.database

import android.app.Application
import android.content.Context
import com.notes.example.interfaces.MainInterface

class DeletingNote(val context: Context) {
    private val appDatabase = AppDatabase.getInstance(Application())
    fun deleteNote(id: Int?) {
        appDatabase.noteContentDao().deleteNote(id)
        DBChange.DBChange.dbWasChanged = true // todo
    }
}