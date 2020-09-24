package com.notes.example.adapters

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.notes.example.R
import com.notes.example.database.ContentPOJO
import com.notes.example.interfaces.MainInterface
import com.notes.example.presenters.MainPresenter
import com.notes.example.utils.AllViews
import kotlinx.android.synthetic.main.activity_content.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.Comparator

class RecyclerViewAdapter(val context: MainInterface,
                          val res: Resources,
                          private val notesList: ArrayList<ContentPOJO>)
    : RecyclerView.Adapter<AllViews>() {
    lateinit var onNoteClickListener: MainPresenter.OnNoteClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViews {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_content, parent, false)
        return AllViews(view)
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalCoroutinesApi
    override fun onBindViewHolder(holder: AllViews, position: Int) {

        with(holder) {
            with(notesList[position]) {
                dateCreateNote.text = created.subSequence(0..created.length-4)
                dateEditNote.text = edited.subSequence(0..edited.length-4)
                titleNote.text = title
                if (text.length > 160) {
                    textNote.text = "${text.subSequence(0..160)} [ ... ]"
                } else {
                    textNote.text = text
                }

                progressBarDelete.visibility = View.INVISIBLE
                buttonX.setImageResource(R.drawable.delete_black)
                buttonEdit.visibility = View.GONE
                buttonDelete.visibility = View.GONE
                if (created == edited) textLastChange.text = context.context.getString(R.string.no_change)
                else dateEditNote.setTextColor(Color.parseColor("#000000"))
                //layoutDateEdit.visibility = View.GONE
                textNote.setTextIsSelectable(false)
                titleNote.setTextIsSelectable(false)

                itemView.card_view_note.setOnClickListener {
                    context.id = id
                    context.dateTimeCreated = created
                    context.title = title
                    context.view = itemView
                    context.text = text
                    context.dateTimeEdited = edited
                    onNoteClickListener.onNoteClick(this)
                }
                buttonX.setOnClickListener {
                    context.id = id
                    context.title = title
                    context.view = itemView
                    context.showConfirmationDialog()
                }
                buttonQuickEdit.setOnClickListener {
                    context.id = id
                    context.dateTimeCreated = created
                    context.dateTimeEdited = edited
                    context.title = title
                    context.text = text
                    context.startEditActivity(id, created, title, text, edited)
                }
                buttonCreatePDF.setOnClickListener {
                    context.dateTimeCreated = created
                    context.title = title
                    context.text = text
                    context.dateTimeEdited = edited
                    context.createPDF()
                }
                buttonShare.setOnClickListener {
                    context.title = title
                    context.text = text
                    context.shareNote(title, text)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

}
