package com.notes.example.interfaces

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout

interface ContentInterface {
    var context: Context
    var resourcesContentNote: Resources
    var view: View
    var id: Int?
    var dateTimeCreate: String
    var dateTimeEdit: String
    var title: String
    var text: String

    var buttonEdit: Button
    var buttonDelete: Button
    var buttonQuickEdit: Button
    val buttonCreatePDF: Button
    val buttonShare: Button
    var progressBarDel: ProgressBar
    var createdTextField: TextView
    var editedTextField: TextView
    var layoutDateEdit: LinearLayout
    var textLastChange: TextView
    var titleNoteTextView: TextView
    var textNoteTextView: TextView
    var buttonX: ImageView

    fun fillViewFields(dateTime: String, title: String, text: String, dateTimeEdit: String)
    fun buttonsAssign(id: Int?, dateTime: String, title: String, text: String, dateTimeEdit: String)
    fun editingNoteLoad(id: Int?, dateTime: String, title: String, text: String, dateTimeEdit: String)
    fun createPDF(dateTime: String, title: String, text: String)
    fun shareNote(title: String, text: String)
    fun close()
}