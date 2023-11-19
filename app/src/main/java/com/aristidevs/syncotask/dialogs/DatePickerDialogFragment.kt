package com.aristidevs.syncotask.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.aristidevs.syncotask.R
import java.util.Calendar

class DatePickerDialogFragment(private val listener: (year: Int, month: Int, day: Int, textTimeView: EditText) -> Unit): DialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var textDateView: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Utiliza la fecha actual para los valores por default del Picker Dialog
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Crea una nueva instancia de DatePickerDialog y la regresa
        return DatePickerDialog(requireActivity(), R.style.DatePicker_createTask_SyncOTask, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        listener(year, month, day, textDateView)
    }

    fun setTextTimeView(editText: EditText) {
        textDateView = editText
    }
}
