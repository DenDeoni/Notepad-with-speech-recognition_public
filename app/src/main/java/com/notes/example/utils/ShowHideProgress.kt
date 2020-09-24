package com.notes.example.utils

import android.view.View

import android.widget.ProgressBar
import com.notes.example.R

class ShowHideProgress(rootView: View) {

    private val progressBar: ProgressBar = rootView.findViewById(R.id.progress_bar_delete)

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
}