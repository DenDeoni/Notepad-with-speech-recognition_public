package com.notes.example.interfaces

import android.content.Context
import android.content.Intent
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText

interface EditCreateInterface {
    var context: Context
    var id: Int?
    var dateTimeCreate: String
    var dateTimeEdit: String
    var title: String
    var text: String
    var loadedTitle: String
    var loadedText: String
    var intents: Intent

    var buttonClose: ImageView
    var buttonSave: Button
    var buttonSaveCopy: Button
    var buttonEraseTitle: Button
    var buttonEraseText: Button
    val buttonCreatePDF: Button
    val buttonShare: Button
    var progressBar: ProgressBar
    var recProgressTitle: ProgressBar
    var recProgressText: ProgressBar
    var micTitle: ToggleButton
    var micText: ToggleButton
    var inputTitle: TextInputEditText
    var inputText: TextInputEditText
    var spinnerLangSelect: Spinner
    var micButton: ToggleButton
    var infoButton: Button

    var supportFRGM: FragmentManager
    val infoFragment: Fragment

    fun buttonsAssign()
    fun close()
    fun intentResponse()
    fun createPDF(dateTime:String, title:String, text:String)
    fun shareNote()
}