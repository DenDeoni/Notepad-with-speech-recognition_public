package com.notes.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "notes")
data class Notes(
        @PrimaryKey
        @SerializedName("ID")
        @Expose
        val id: String,
        @SerializedName("TITLE")
        @Expose
        val title: String?,
        @SerializedName("TEXT")
        @Expose
        val text: String?
)