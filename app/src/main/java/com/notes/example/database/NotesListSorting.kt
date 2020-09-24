package com.notes.example.database

import com.notes.example.constans.BY_CHANGE
import com.notes.example.constans.BY_DATE
import com.notes.example.interfaces.MainInterface
import com.notes.example.utils.Arrange
import java.text.SimpleDateFormat
import java.util.*

class NotesListSorting(private val mainInterface: MainInterface, private val notesList: ArrayList<ContentPOJO>) {

    private fun notesListSort(sortBy: String, notesList: ArrayList<ContentPOJO>): ArrayList<ContentPOJO> {
        val parser = SimpleDateFormat("d.M.yyyy   HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        when (sortBy) {
            BY_DATE -> {
                notesList.sortWith(compareBy {
                    formatter.format(parser.parse(it.created) ?: it.created)
                })
            }
            BY_CHANGE -> {
                notesList.sortWith(compareBy {
                    formatter.format(parser.parse(it.edited) ?: it.edited)
                })
            }
            else -> {
                notesList.sortWith(compareBy {
                    it.title
                })
            }
        }
        return notesList
    }

    fun notesListOrder(sortBy: String): ArrayList<ContentPOJO> {
        val ns = notesListSort(sortBy, notesList)
        val orderAsc = Arrange(mainInterface.sharedPrefs).asc()

        if (!orderAsc) {
            ns.reverse()
        }
        return ns
    }
}