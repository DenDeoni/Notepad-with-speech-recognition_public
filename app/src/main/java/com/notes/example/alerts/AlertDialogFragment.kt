package com.notes.example.alerts

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.notes.example.R

class AlertDialogFragment(currentContext: Context,
                          message: String,
                          confirmationListenerButtons: ConfirmationListenerButtons
) : DialogFragment() {
    private val dialog: AlertDialog = AlertDialog.Builder(currentContext)
            .setMessage(message)
            .setPositiveButton(R.string.yes_button_text) { _, _ ->
                confirmationListenerButtons.positiveButtonClicked()
            }
            .setNegativeButton(R.string.no_button_text) { _, _ ->
                confirmationListenerButtons.negativeButtonClicked()
            }.create()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setBtnColor()
        return dialog
    }

    fun setBtnColor() {
        dialog.setOnShowListener {
            dialog.getButton(BUTTON_POSITIVE).setTextColor(Color.parseColor("#df1c1c"))
            dialog.getButton(BUTTON_NEGATIVE).setTextColor(Color.parseColor("#26c900"))
        }
    }

    fun hideNegativeButton(){
        dialog.setOnShowListener {
            dialog.getButton(BUTTON_NEGATIVE).visibility = View.GONE
        }
    }
}

interface ConfirmationListenerButtons {
    fun positiveButtonClicked()
    fun negativeButtonClicked()
}