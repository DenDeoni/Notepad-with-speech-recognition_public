package com.notes.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class ContentPOJO(
        @PrimaryKey(autoGenerate = false)
        var id: Int?,
        var created: String = "",
        var title: String = "",
        var text: String = "",
        var edited: String = ""
)