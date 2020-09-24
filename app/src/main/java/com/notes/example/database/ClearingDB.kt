package com.notes.example.database

import com.notes.example.interfaces.MainInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ClearingDB(val context: MainInterface) {
    fun clearDB() {
        val appDB = AppDatabase.getInstance(context.context)
        CoroutineScope(Dispatchers.IO).launch {
            appDB.clearAllTables()
            //appDB.noteContentDao().dropNotesTable()
        }
        context.renewListNotes()
    }
}
