package com.notes.example.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.notes.example.R
import com.notes.example.alerts.ToastAlert
import com.notes.example.constans.READ_CLOUD
import com.notes.example.constans.USER_ID
import com.notes.example.database.ClearingDB
import com.notes.example.database.ContentPOJO
import com.notes.example.database.LoadData
import com.notes.example.database.WritingNoteToDB
import com.notes.example.interfaces.MainInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FireStoreNetwork(main: MainInterface) {

    private val mainContext = main
    private val cloudDB = FirebaseFirestore.getInstance()
    private val userID = mainContext.sharedPrefs.getString(USER_ID, "")
    private var notesIDs = ArrayList<String>()
    var notesList = ArrayList<ContentPOJO>()


    @ExperimentalStdlibApi
    fun choiceAction(flag: String) {
        readCloud(flag)
    }

    @ExperimentalStdlibApi
    private fun readCloud(flag: String) { // return result
        userID?.let {
            cloudDB
                    .collection("users")
                    .document(it)
                    .collection("notes")
        }
                ?.get()
                ?.addOnSuccessListener {
                    val result = it
                    if (flag == READ_CLOUD) writeDB(result)
                    else writeCloud(result)
                }
                ?.addOnFailureListener { _ ->
                    val message = mainContext.context.getString(R.string.cannot_read)
                    ToastAlert(mainContext.context, message).toastAlert()
                }
    }

    private fun parseCloudResponse(result: QuerySnapshot): Pair<ArrayList<ContentPOJO>, ArrayList<String>> {

        for (document in result) {
            val note = ContentPOJO(document.id.toInt())
            note.id = document.id.toInt()
            note.created = document.data.getValue("dateCreate").toString()
            note.title = Encryption(mainContext).decrypt(document.data.getValue("title").toString())
            note.text = Encryption(mainContext).decrypt(document.data.getValue("text").toString())
            note.edited = document.data.getValue("dateChange").toString()
            notesList.add(note)
            notesIDs.add(document.id)
        }
        return Pair(notesList, notesIDs)
    }

    private fun writeDB(result: QuerySnapshot) {

       if (!checkCloudContent(result)) return

        CoroutineScope(Dispatchers.Main).launch { // waiting for coroutine job
            withContext(Dispatchers.IO) {
                WritingNoteToDB(mainContext.context).writeNoteToDB(parseCloudResponse(result).first)
            }
            val message = mainContext.context.getString(R.string.success_write_db)
            ToastAlert(mainContext.context, message).toastAlert()
            mainContext.renewListNotes()
        }
    }

    private fun checkCloudContent(result: QuerySnapshot): Boolean {
        return when {
            (result.size() != 0) -> {
                ClearingDB(mainContext).clearDB()
                true
            }
            else -> {
                val message = mainContext.context.getString(R.string.cloud_empty)
                ToastAlert(mainContext.context, message).toastAlert()
                false
            }
        }
    }

    @ExperimentalStdlibApi
    private fun writeCloud(result: QuerySnapshot) {

        val dbColl = cloudDB.collection("users")
        val notes = userID?.let {
            cloudDB
                    .collection("users")
                    .document(it)
                    .collection("notes")
        }

        val sizeNotesList = parseCloudResponse(result).second.size

        for (i in 0 until sizeNotesList) {
            val id = notesIDs[i]
            if (notesIDs.contains(id)) {
                notes?.document(id)?.delete()
            }
        }
        if (userID != null) {
            dbColl.document().delete()
        }

        CoroutineScope(Dispatchers.IO).launch {
            var listPOJO: List<ContentPOJO>

            withContext(Dispatchers.Default) {
                listPOJO = LoadData(mainContext).loadData()
            }

            (listPOJO.indices).forEach { i ->
                val id = listPOJO[i].id
                val data = hashMapOf(
                        "dateCreate" to listPOJO[i].created,
                        "title" to Encryption(mainContext).encrypt(listPOJO[i].title),
                        "text" to Encryption(mainContext).encrypt(listPOJO[i].text),
                        "dateChange" to listPOJO[i].edited)
                notes?.document(id.toString())?.set(data)
            }
        }
        val message = mainContext.context.getString(R.string.success_write_cloud)
        ToastAlert(mainContext.context, message).toastAlert()
    }

}