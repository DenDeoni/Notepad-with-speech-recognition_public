package com.notes.example.presenters

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.notes.example.R
import com.notes.example.alerts.AlertDialogFragment
import com.notes.example.alerts.ConfirmationListenerButtons
import com.notes.example.database.ContentPOJO
import com.notes.example.database.DBChange
import com.notes.example.database.WritingNoteToDB
import com.notes.example.interfaces.EditCreateInterface
import com.notes.example.utils.CreatorPDF
import com.notes.example.utils.DateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class EditCreatePresenter(private val editCreateInterface: EditCreateInterface)
    : ConfirmationListenerButtons {

    private fun close() {
        editCreateInterface.close()
    }

    fun buttonsAssign() {
        editCreateInterface.buttonClose.setImageResource(R.drawable.close_black)
        editCreateInterface.buttonClose.setOnClickListener {
            if (editCreateInterface.inputTitle.text.toString() != editCreateInterface.loadedTitle ||
                    editCreateInterface.inputText.text.toString() != editCreateInterface.loadedText)
                showConfirmationDialog()
            else close()
        }
        editCreateInterface.buttonSave.setOnClickListener {
            saveNote(saveCopy = false)
        }
        editCreateInterface.buttonSaveCopy.setOnClickListener {
            saveNote(saveCopy = true)
        }

        editCreateInterface.infoButton.setOnClickListener {
            showInfo()
        }
    }

    private fun showInfo() {
        editCreateInterface.supportFRGM.beginTransaction()
                .add(R.id.container_info_fragment, editCreateInterface.infoFragment)
                .commit()
    }

    fun detachInfoFragment() {
        val ifFragmentIsLoaded = editCreateInterface.supportFRGM.findFragmentById(R.id.container_info_fragment) != null
        if (ifFragmentIsLoaded) {
            editCreateInterface.supportFRGM.beginTransaction()
                    .remove(editCreateInterface.infoFragment)
                    .commit()
        } else {
            editCreateInterface.close()
        }
    }


    private fun saveNote(saveCopy: Boolean) {

        editCreateInterface.progressBar.visibility = View.VISIBLE
        val contentPOJO: ContentPOJO
        if (saveCopy) {
            editCreateInterface.id = null // save new item in DB
            contentPOJO = ContentPOJO(editCreateInterface.id)
            contentPOJO.created = DateTime().getDateTimeString()
            contentPOJO.edited = contentPOJO.created
        } else {
            contentPOJO = ContentPOJO(editCreateInterface.id)
            contentPOJO.created = getDateCreated()
            contentPOJO.edited = getDateEdited()
        }
        contentPOJO.title = editCreateInterface.inputTitle.text.toString()
        contentPOJO.text = editCreateInterface.inputText.text.toString()

        val dataForInserting = arrayListOf(contentPOJO)

        CoroutineScope(Dispatchers.IO).launch {
            WritingNoteToDB(editCreateInterface.context).writeNoteToDB(dataForInserting)
            cancel()
        }
        if (!saveCopy) {
            editCreateInterface.intentResponse()
        }
        DBChange.DBChange.dbWasChanged = true
        close()
    }

    private fun getDateCreated(): String {
        return if (editCreateInterface.dateTimeCreate.isNotEmpty()) {
            editCreateInterface.dateTimeCreate
        } else {
            DateTime().getDateTimeString()
        }

    }

    private fun getDateEdited(): String {
        return if (editCreateInterface.dateTimeCreate.isNotEmpty()) {
            return DateTime().getDateTimeString()
        } else {
            DateTime().getDateTimeString() //""
        }
    }

    private fun showConfirmationDialog() {
        val message = editCreateInterface.context.getString(R.string.close_w_o_saving)
        AlertDialogFragment(editCreateInterface.context, message, this)
                .onCreateDialog(Bundle()).show()

        AlertDialogFragment(editCreateInterface.context, message, this)
                .setBtnColor()
    }

    override fun positiveButtonClicked() {
        close()
    }

    override fun negativeButtonClicked() {
    }

    fun clickBtnShareNote() {
        with(editCreateInterface) {
            buttonShare.setOnClickListener {
                title = inputTitle.editableText.toString()
                text = inputText.editableText.toString()
                shareNote()
            }
        }
    }

    fun createPDF(dateTime: String, title: String, text: String) {
        CreatorPDF(editCreateInterface.context).createPDF(dateTime, title, text, "")
    }

    fun shareNote(title: String, text: String) {

        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$text")
            putExtra(Intent.EXTRA_TITLE, title)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, editCreateInterface.context.getString(R.string.send_by))
        editCreateInterface.context.startActivity(share)

    }


}