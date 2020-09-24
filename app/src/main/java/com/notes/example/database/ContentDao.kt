package com.notes.example.database

import androidx.room.*

@Dao
interface ContentDao {

    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getNotesList(): List<ContentPOJO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(contentPOJO: List<ContentPOJO>)

    @Update
    fun updateNote(contentPOJO: List<ContentPOJO>)

    @Query("DELETE FROM notes WHERE id =:id")
    fun deleteNote(id: Int?)

    @Query("DELETE FROM notes")
    fun dropNotesTable()

}