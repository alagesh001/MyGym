package com.oxad.mygym.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.oxad.mygym.R

class ProgressDialogUtil(private val context: Context) {
    private var alertDialog: AlertDialog? = null

    fun showLoadingDialog() {
        if (alertDialog != null && alertDialog!!.isShowing) {
            return // Prevent multiple dialogs
        }

        val builder = AlertDialog.Builder(context, R.style.TransparentDialog) // Apply theme
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_progress, null)

        builder.setView(dialogView)
        builder.setCancelable(false) // Prevent dismissing by tapping outside
        alertDialog = builder.create()
        alertDialog?.show()
    }

    fun hideLoadingDialog() {
        alertDialog?.dismiss()
    }
}