package com.notes.example.database

import com.notes.example.interfaces.MainInterface
import com.notes.example.utils.Arrange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesListCreating(private val mainInterface: MainInterface) {

    fun notesListCreate() {
        CoroutineScope(Dispatchers.Main).launch {

            val notesList: ArrayList<ContentPOJO> = ArrayList()
            var listPOJO: List<ContentPOJO>
            withContext(Dispatchers.IO) {
                listPOJO = LoadData(mainInterface).loadData()
            }
            (listPOJO.indices).forEach { i ->
                val card = ContentPOJO(i)
                card.id = listPOJO[i].id
                card.created = listPOJO[i].created
                card.title = listPOJO[i].title
                card.text = listPOJO[i].text
                card.edited = listPOJO[i].edited
                notesList.add(card)
            }
            val sortBy = Arrange(mainInterface.sharedPrefs).sortBy()
            val notesSorted = NotesListSorting(mainInterface, notesList).notesListOrder(sortBy)
            mainInterface.attachAdapter(notesSorted)
     }
    }
}