package com.aristidevs.syncotask.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.aristidevs.syncotask.R

class CreateTagDialog(private val context: Context) {

    fun showTagInputDialog(onTagEntered: (String) -> Unit) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_tag, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.findViewById<EditText>(R.id.dialog_textName).requestFocus()

        mDialogView.findViewById<Button>(R.id.dialog_btnSubmit).setOnClickListener {
            val tagName = mDialogView.findViewById<EditText>(R.id.dialog_textName).text.toString()
            mAlertDialog.dismiss()
            Toast.makeText(context, "Name: $tagName", Toast.LENGTH_SHORT).show()
            onTagEntered(tagName)
        }

        mDialogView.findViewById<Button>(R.id.dialog_btnCancel).setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
}