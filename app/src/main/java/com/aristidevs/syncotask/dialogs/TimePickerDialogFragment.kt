package com.aristidevs.syncotask.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.aristidevs.syncotask.R
import java.util.Calendar

class TimePickerDialogFragment(private val listener: (hour: Int, minute: Int, textTimeView: EditText) -> Unit): DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private lateinit var textTimeView: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Utiliza la hora actual para los valores por default del Picker Dialog
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Crea una nueva instancia de TimePickerDialog y la regresa
        return TimePickerDialog(activity, R.style.TimePicker_createTask_SyncOTask, this, hour, minute, false)
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        listener(hour, minute, textTimeView)
    }

    fun setTextTimeView(editText: EditText) {
        textTimeView = editText
    }
}
