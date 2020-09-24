package com.notes.example.utils

import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_content.view.*

class AllViews(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dateCreateNote: TextView = itemView.created_text_field
    val titleNote: TextView = itemView.title_note
    val textNote: TextView = itemView.text_note
    val dateEditNote: TextView = itemView.edited_text_field
    val buttonX: ImageView = itemView.button_close
    val buttonEdit: Button = itemView.button_edit
    val buttonDelete: Button = itemView.button_delete
    val buttonQuickEdit: Button = itemView.button_quick_edit
    val buttonCreatePDF: Button = itemView.button_create_pdf
    val buttonShare: Button = itemView.button_share_note
    val progressBarDelete: ProgressBar = itemView.progress_bar_delete
    val layoutDateEdit: LinearLayout = itemView.layout_date_change
    val textLastChange: TextView = itemView.last_change_text
}