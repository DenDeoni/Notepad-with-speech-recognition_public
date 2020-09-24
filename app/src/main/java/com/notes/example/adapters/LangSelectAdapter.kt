package com.notes.example.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.notes.example.R
import com.notes.example.interfaces.EditCreateInterface
import javax.annotation.Resource

class LangSelectAdapter(val context: Context,
                        contextEditCreate: EditCreateInterface,
                        val res: Resource,
                        val langList: ArrayList<String>) {

    val adapter = ArrayAdapter.createFromResource(
            context,
            R.array.lang_list,
            android.R.layout.list_content)
}