package com.notes.example.database

import com.notes.example.interfaces.MainInterface
import com.notes.example.utils.Arrange
import java.util.stream.Collectors.toCollection

class NotesList(private val mainInterface: MainInterface) {
    fun notesListConnect() {
        NotesListCreating(mainInterface).notesListCreate()
    }

}