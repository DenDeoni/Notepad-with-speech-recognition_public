package com.notes.example.presenters

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.notes.example.R
import com.notes.example.alerts.AlertDialogFragment
import com.notes.example.alerts.ConfirmationListenerButtons
import com.notes.example.database.ControlDeleting
import com.notes.example.database.DBChange
import com.notes.example.database.DeletingNote
import com.notes.example.interfaces.ContentInterface
import com.notes.example.interfaces.MainInterface
import com.notes.example.utils.CreatorPDF
import com.notes.example.utils.ShowHideProgress
import kotlinx.coroutines.*

class ContentPresenter(private val contentInterface: ContentInterface)
    : ConfirmationListenerButtons {

    fun fillViewFields(dateTime: String, title: String, text: String, dateTimeEdit: String) {
        with(contentInterface) {
            buttonX.setImageResource(R.drawable.close_black)
            buttonX.setColorFilter(0x000000)
            createdTextField.text = dateTime.subSequence(0..dateTime.length-4)
            titleNoteTextView.text = title
            textNoteTextView.text = text
            editedTextField.text = dateTimeEdit.subSequence(0..dateTimeEdit.length-4)
            dateTimeEditVisible(dateTime, dateTimeEdit)
            if (dateTime == dateTimeEdit) textLastChange.text = context.getString(R.string.no_change)
            else editedTextField.setTextColor(Color.parseColor("#000000"))
        }
    }

    private fun dateTimeEditVisible(dateTime: String, dateTimeEdit: String) {
        if (dateTimeEdit == dateTime) contentInterface.textLastChange.text = contentInterface.context.getString(R.string.no_change)
        //contentInterface.layoutDateEdit.visibility = View.GONE
    }

    fun buttonsAssign(id: Int?, dateTime: String, title: String, text: String, dateTimeEdit: String) {
        with(contentInterface) {
            buttonX.setOnClickListener {
                contentInterface.close()
            }
            buttonEdit.setOnClickListener {
                contentInterface.editingNoteLoad(id, dateTime, title, text, dateTimeEdit)
            }
            buttonDelete.setOnClickListener {
                showConfirmationDialog()
            }
        }
    }

    private fun showConfirmationDialog() {
        val message = contentInterface.context.getString(R.string.want_to_delete)
        AlertDialogFragment(contentInterface.context, message, this)
                .onCreateDialog(Bundle())
                .show()

        AlertDialogFragment(contentInterface.context, message, this)
                .setBtnColor()
    }

    @ExperimentalCoroutinesApi
    override fun positiveButtonClicked() {
        showProgress(contentInterface.view)
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            withContext(Dispatchers.Default) {
                DeletingNote(contentInterface.context).deleteNote(contentInterface.id)
            }
            ControlDeleting(contentInterface.context, contentInterface.title).controlDeleting()
           DBChange.DBChange.dbWasChanged = true // todo
            contentInterface.close()
        }
    }
    override fun negativeButtonClicked() {
    }

    private fun showProgress(view: View) {
        ShowHideProgress(view).showProgressBar()
    }

    fun clickBtnCreatePDF() {
        with(contentInterface) {
            buttonCreatePDF.setOnClickListener {
                dateTimeCreate = dateTimeCreate
                title = title
                text = text
                createPDF(dateTimeCreate, title, text)
            }
        }
    }

    fun clickBtnShareNote() {
        with(contentInterface) {
            buttonShare.setOnClickListener {
                title = title
                text = text
                shareNote(title, text)
            }
        }
    }

    fun createPDF(dateTime: String, title: String, text: String) {
        CreatorPDF(contentInterface.context).createPDF(dateTime, title, text, "")
    }

    fun shareNote(title: String, text: String) {

        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$text")
            putExtra(Intent.EXTRA_TITLE, title)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, contentInterface.context.getString(R.string.send_by))
        contentInterface.context.startActivity(share)

    }


}