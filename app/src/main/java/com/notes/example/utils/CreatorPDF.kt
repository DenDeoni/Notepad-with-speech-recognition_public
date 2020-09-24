package com.notes.example.utils

import android.app.Activity
import android.content.Context
import android.os.Environment
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.BaseFont.IDENTITY_H
import com.itextpdf.text.pdf.BaseFont.createFont
import com.itextpdf.text.pdf.PdfWriter
import com.notes.example.R
import com.notes.example.alerts.ToastAlert
import java.io.FileOutputStream

class CreatorPDF(val context: Context) {

    fun createPDF(dateTimeCreated: String, title: String, text: String, dateTimeEdited: String) {
        val doc = Document()
        
        val currentTime = DateTime().getDateTimeString()
        var titleTemp = title
        if(title.isEmpty()) titleTemp = context.getString(R.string.doc)
        val fileName = "$titleTemp-$currentTime"
        val path = Environment.getExternalStorageDirectory()
        val pathFileName = "$path/$fileName.pdf"
        val font = "/assets/times.ttf"
        val fontBold = "/assets/ARIALNB.TTF"
        val baseFont: BaseFont = createFont(font, IDENTITY_H, true)
        val baseFontBold: BaseFont = createFont(fontBold, IDENTITY_H, true)
        val dateTimeText = dateTimeCreated.subSequence(0..dateTimeCreated.length - 4).toString()
        val changed = context.getString(R.string.changed)
        var dateTimeEditedText = ""
        if(dateTimeCreated != dateTimeEdited && dateTimeEdited.isNotEmpty()){
            dateTimeEditedText = changed + dateTimeEdited.subSequence(0..dateTimeEdited.length - 4).toString()
        }

        val dateTimeField = Paragraph(dateTimeText, Font(baseFont, 22F))
        dateTimeField.alignment = Element.ALIGN_RIGHT
        val titleField = Paragraph("\n$title\n\n", Font(baseFontBold, 32F))
        titleField.alignment = Element.ALIGN_CENTER
        val dateTimeEditedField = Paragraph(dateTimeEditedText, Font(baseFont, 22F))
        dateTimeEditedField.alignment = Element.ALIGN_RIGHT

        try {
            PdfWriter.getInstance(doc, FileOutputStream(pathFileName))
            doc.open()
            doc.add(dateTimeField)
            doc.add(titleField)
            doc.add(Paragraph("$text\n\n", Font(baseFont, 26F)))
            doc.add(dateTimeEditedField)
            doc.close()
            val stringSavedTo = context.getString(R.string.saved_to)
            val internalStorage = context.getString(R.string.internal_storage)
            val message = "$stringSavedTo\n$internalStorage"
            ToastAlert(context, message).toastAlert()

        } catch (e: Exception) {
            var message = ""
            e.message?.let { message = it }
            ToastAlert(context, message).toastAlert()
        }
    }
}
