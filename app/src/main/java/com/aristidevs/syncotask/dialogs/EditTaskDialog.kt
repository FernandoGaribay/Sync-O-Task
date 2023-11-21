package com.aristidevs.syncotask.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.aristidevs.syncotask.R

class EditTaskDialog(private val context: Context) {

    fun showEditTaskDialog() {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_tag, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
    }
}