package com.notes.example.database

import com.notes.example.interfaces.MainInterface

class LoadData(private val mainInterface: MainInterface) {

    fun loadData(): List<ContentPOJO> {

        return mainInterface
                .appDatabase
                .noteContentDao()
                .getNotesList()
    }
}