package com.notes.example.interfaces

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.notes.example.database.AppDatabase
import com.notes.example.database.ContentPOJO
import com.notes.example.utils.ShowHideProgress

interface MainInterface {
    fun initFields()
    fun createButtonClick()
    fun authorButtonClick()
    fun menuButtonClick()
    fun infoButtonClick()
    fun buttonsMenuInit()
    fun attachAdapter(notesList: ArrayList<ContentPOJO>)
    fun showToast(title: String, message: String)
    fun hideProgress(view: View)
    fun showConfirmationDialog()
    fun contentNoteIntent()
    fun startEditActivity(id: Int?, dateTime: String, title: String, text: String, dateTimeEdit: String)
    fun createPDF()
    fun shareNote(title: String, text: String)
    fun renewListNotes()


    var sharedPrefs: SharedPreferences
    var editor: SharedPreferences.Editor
    var appDatabase: AppDatabase
    var id: Int?
    var dateTimeCreated: String
    var dateTimeEdited: String
    var title: String
    var text: String
    var view: View
    var context: Context
    var res: Resources
    var intentMain: Intent

    val notesListRView: RecyclerView
    val progressBar: ProgressBar
    val buttonCreate: Button
    val buttonAuthor: Button
    val buttonInfoCLose: Button
    val floatingActionButton: FloatingActionButton
    val floatingInfoButton: FloatingActionButton
    val builder: AlertDialog.Builder
    val showHideProgress: ShowHideProgress
    val infoFragment: Fragment
    val authFragment: Fragment
    var supportFRGM: FragmentManager
}