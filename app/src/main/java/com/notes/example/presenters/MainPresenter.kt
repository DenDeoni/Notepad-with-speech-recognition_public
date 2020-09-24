package com.notes.example.presenters

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.notes.example.R
import com.notes.example.adapters.RecyclerViewAdapter
import com.notes.example.alerts.AlertDialogFragment
import com.notes.example.alerts.ConfirmationListenerButtons
import com.notes.example.constans.*
import com.notes.example.database.ContentPOJO
import com.notes.example.database.ControlDeleting
import com.notes.example.database.DeletingNote
import com.notes.example.interfaces.MainInterface
import com.notes.example.utils.CreatorPDF
import com.notes.example.utils.ShowHideProgress
import com.notes.example.views.ContentActivity
import com.notes.example.views.EditionCreationActivity
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

open class MainPresenter(private val mainInterface: MainInterface) : ConfirmationListenerButtons, Fragment() {

    interface OnNoteClickListener {
        fun onNoteClick(contentPOJO: ContentPOJO)
    }

    fun startCreatingActivity() {
        mainInterface.buttonCreate.setOnClickListener {
            val intent = Intent(mainInterface.context, EditionCreationActivity::class.java)
            intent.putExtra(NEW_NOTE, 0)
            startActivity(mainInterface.context, intent, Bundle.EMPTY)
        }
    }

    fun showInfo() {
        mainInterface.floatingInfoButton.setOnClickListener {
            mainInterface.supportFRGM.beginTransaction()
                    .add(R.id.container_fragment, mainInterface.infoFragment)
                    .commit()
        }
    }

    fun detachInfoFragment() {
        mainInterface.supportFRGM.beginTransaction()
                .remove(mainInterface.infoFragment)
                .commit()
    }

    fun editActivityLoad() {
        val intent = Intent(mainInterface.context, EditionCreationActivity::class.java)
        intent.putExtra(ID, mainInterface.id)
        intent.putExtra(DATE_TIME, mainInterface.dateTimeCreated)
        intent.putExtra(TITLE, mainInterface.title)
        intent.putExtra(TEXT, mainInterface.text)
        if (mainInterface.dateTimeEdited.isEmpty()) {
            intent.putExtra(DATE_TIME_EDIT, mainInterface.dateTimeCreated) //"")
        } else {
            intent.putExtra(DATE_TIME_EDIT, mainInterface.dateTimeEdited)
        }
        startActivity(mainInterface.context, intent, Bundle.EMPTY)
    }

    fun contentActivityLoad() { // invoke from RecyclerView
        val intent = Intent(mainInterface.context, ContentActivity::class.java)
        intent.putExtra(ID, mainInterface.id)
        intent.putExtra(DATE_TIME, mainInterface.dateTimeCreated)
        intent.putExtra(TITLE, mainInterface.title)
        intent.putExtra(TEXT, mainInterface.text)
        if (mainInterface.dateTimeEdited.isEmpty()) {
            intent.putExtra(DATE_TIME_EDIT, mainInterface.dateTimeCreated) //mainInterface.mainContext.getString(R.string.no_change))
        } else {
            intent.putExtra(DATE_TIME_EDIT, mainInterface.dateTimeEdited)
        }
        startActivity(mainInterface.context, intent, Bundle.EMPTY)
    }

    fun btnAuthorClick() {
        mainInterface.buttonAuthor.setOnClickListener {
            val sendMail = Intent(Intent.ACTION_SENDTO)
            val mail = mainInterface.context.getString(R.string.e_mail)
            val subject = mainInterface.context.getString(R.string.subject)
            val urlString = "mailto:${Uri.encode(mail)}?subject=${Uri.encode(subject)}"
            sendMail.data = Uri.parse(urlString)
            if (sendMail.resolveActivity(mainInterface.context.packageManager) != null) {
                startActivity(mainInterface.context, sendMail, Bundle.EMPTY)
            }
        }
    }

    fun attachAdapter(notesList: ArrayList<ContentPOJO>) {
        val adapter = RecyclerViewAdapter(
                mainInterface,
                mainInterface.res,
                notesList)
        val notesListRView = mainInterface.notesListRView
        notesListRView.adapter = adapter
        mainInterface.progressBar.visibility = View.GONE
        adapter.onNoteClickListener = object : OnNoteClickListener {
            override fun onNoteClick(contentPOJO: ContentPOJO) {
                mainInterface.contentNoteIntent()
            }
        }
    }


    fun showConfirmationDialog() {
        val message = mainInterface.context.getString(R.string.want_to_delete)
        AlertDialogFragment(mainInterface.context, message, this)
                .onCreateDialog(Bundle()).show()
        AlertDialogFragment(mainInterface.context, message, this)
                .setBtnColor()
    }

    @ExperimentalCoroutinesApi
    override fun positiveButtonClicked() {
        showProgress(mainInterface.view)
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            withContext(Dispatchers.Default) {
                DeletingNote(mainInterface.context).deleteNote(mainInterface.id)
            }
            ControlDeleting(mainInterface.context, mainInterface.title).controlDeleting()
           mainInterface.renewListNotes() // ToDo
        }
    }

    override fun negativeButtonClicked() {

    }

    private fun showProgress(view: View) {
        ShowHideProgress(view).showProgressBar()
    }

    fun hideProgress(view: View) {
        ShowHideProgress(view).hideProgressBar()
    }

    fun createPDF(dateTime: String, title: String, text: String, dateTimeEdited: String) {
        CreatorPDF(mainInterface.context).createPDF(dateTime, title, text, dateTimeEdited)
    }

    fun shareNote(title: String, text: String) {

        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$text")
            putExtra(Intent.EXTRA_TITLE, title)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, mainInterface.context.getString(R.string.send_by))
        mainInterface.context.startActivity(share)
    }
}