package com.notes.example.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.notes.example.R
import com.notes.example.alerts.ToastAlert
import com.notes.example.constans.*
import com.notes.example.interfaces.ContentInterface
import com.notes.example.presenters.ContentPresenter
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ContentActivity : AppCompatActivity(), ContentInterface {

    private val contentPresenter: ContentPresenter = ContentPresenter(this)
    override lateinit var context: Context
    override lateinit var resourcesContentNote: Resources
    override var id: Int? = null
    override lateinit var dateTimeCreate: String
    override lateinit var dateTimeEdit: String
    override lateinit var title: String
    override lateinit var text: String
    override lateinit var buttonEdit: Button
    override lateinit var buttonDelete: Button
    override lateinit var buttonQuickEdit: Button
    override lateinit var buttonCreatePDF: Button
    override lateinit var buttonShare: Button
    override lateinit var progressBarDel: ProgressBar
    override lateinit var createdTextField: TextView
    override lateinit var editedTextField: TextView
    override lateinit var titleNoteTextView: TextView
    override lateinit var textNoteTextView: TextView
    override lateinit var layoutDateEdit: LinearLayout
    override lateinit var textLastChange: TextView
    override lateinit var buttonX: ImageView
    override lateinit var view: View

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        getIntents()
        initFields()
        clickBtnCreatePDF()
        clickBtnShareNote()
    }

    private fun getIntents() {
        intent.getIntExtra(ID, 0).let { id = it }
        intent.getStringExtra(DATE_TIME)?.let { dateTimeCreate = it }
        intent.getStringExtra(TITLE)?.let { title = it }
        intent.getStringExtra(TEXT)?.let { text = it }
        intent.getStringExtra(DATE_TIME_EDIT)?.let { dateTimeEdit = it }
    }


    private fun initFields() {
        context = this
        resourcesContentNote = resources
        buttonEdit = button_edit
        buttonDelete = button_delete
        buttonQuickEdit = button_quick_edit
        buttonCreatePDF = button_create_pdf
        buttonShare = button_share_note
        progressBarDel = progress_bar_delete
        progressBarDel.visibility = View.INVISIBLE
        buttonQuickEdit.visibility = View.INVISIBLE
        createdTextField = created_text_field
        editedTextField = edited_text_field
        layoutDateEdit = layout_date_change
        textLastChange = last_change_text
        titleNoteTextView = title_note
        textNoteTextView = text_note
        buttonX = button_close
        fillViewFields(dateTimeCreate, title, text, dateTimeEdit)
        buttonsAssign(id, dateTimeCreate, title, text, dateTimeEdit)
        view = progressBarDel
    }

    override fun fillViewFields(dateTime: String, title: String, text: String, dateTimeEdit: String) {
        contentPresenter.fillViewFields(dateTime, title, text, dateTimeEdit)
    }

    override fun buttonsAssign(id: Int?, dateTime: String, title: String, text: String, dateTimeEdit: String) {
        contentPresenter.buttonsAssign(id, dateTime, title, text, dateTimeEdit)
    }

    override fun close() {
        finish()
    }

    override fun editingNoteLoad(id: Int?, dateTime: String, title: String, text: String, dateTimeEdit: String) {
        val intent = Intent(this, EditionCreationActivity::class.java)
        intent.putExtra(ID, id)
        intent.putExtra(DATE_TIME, dateTime)
        intent.putExtra(TITLE, title)
        intent.putExtra(TEXT, text)
        startActivityForResult(intent, REQUEST_CODE_FOR_SAVING, Bundle.EMPTY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == SAVE_CHANGES) close()
    }

    private fun clickBtnCreatePDF() {
        contentPresenter.clickBtnCreatePDF()
    }

    override fun createPDF(dateTime: String, title: String, text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf((Manifest.permission.WRITE_EXTERNAL_STORAGE))
                requestPermissions(permissions, STORAGE_CODE)
            } else {
                contentPresenter.createPDF(dateTime, title, text)
            }
        } else {
            contentPresenter.createPDF(dateTime, title, text)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contentPresenter.createPDF(dateTimeCreate, title, text)
                } else {
                    ToastAlert(context, getString(R.string.permission_denied))
                            .toastAlert()
                }
            }
        }
    }

    private fun clickBtnShareNote() {
        contentPresenter.clickBtnShareNote()
    }

    override fun shareNote(title: String, text: String) {
        contentPresenter.shareNote(title, text)
    }

}
