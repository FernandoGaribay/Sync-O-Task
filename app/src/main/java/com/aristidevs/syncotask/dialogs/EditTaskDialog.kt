package com.aristidevs.syncotask.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.InputType
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.objects.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTaskDialog(private val context: Context, private val fragmentManager: FragmentManager) {

    private lateinit var mAlertDialog: AlertDialog
    private lateinit var title: EditText
    private lateinit var date: EditText
    private lateinit var startTime: EditText
    private lateinit var endTime: EditText
    private lateinit var description: EditText
    private lateinit var linearSubTasks: LinearLayout
    private lateinit var textCreateSubTask: EditText
    private lateinit var linearTags: LinearLayout
    private lateinit var btnAddTag: TextView
    private lateinit var btnLowPriority: LinearLayout
    private lateinit var btnMediumPriority: LinearLayout
    private lateinit var btnHighPriority: LinearLayout
    private lateinit var btnMaxPriority: LinearLayout
    private lateinit var btnEditTask: Button
    private lateinit var btnDeleteTask: Button

    private val newEditTextList = mutableListOf<EditText>()
    private val newTagsList = mutableListOf<TextView>()

    fun showEditTaskDialog(task: Task) {
        initViews(task)
        initListeners(task)
    }

    private fun initViews(task: Task) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null)
        val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
        mAlertDialog = mBuilder.show()

        title = mDialogView.findViewById(R.id.textTitle)
        date = mDialogView.findViewById(R.id.textDate)
        startTime = mDialogView.findViewById(R.id.textStartTime)
        endTime = mDialogView.findViewById(R.id.textEndTime)
        description = mDialogView.findViewById(R.id.textDescription)
        linearSubTasks = mDialogView.findViewById(R.id.linearSubTasks)
        textCreateSubTask = mDialogView.findViewById(R.id.textCreateSubTask)
        linearTags = mDialogView.findViewById(R.id.linearTags)
        btnAddTag = mDialogView.findViewById(R.id.btnAddTag)
        btnLowPriority = mDialogView.findViewById(R.id.btnLowPriority)
        btnMediumPriority = mDialogView.findViewById(R.id.btnMediumPriority)
        btnHighPriority = mDialogView.findViewById(R.id.btnHighPriority)
        btnMaxPriority = mDialogView.findViewById(R.id.btnMaxPriority)
        btnEditTask = mDialogView.findViewById(R.id.btnCreateTask)
        btnDeleteTask = mDialogView.findViewById(R.id.btnDeleteTask)

        initTextViews(task)
    }

    private fun initTextViews(task: Task) {
        title.setText(task.title)
        date.setText(task.date)
        startTime.setText(task.startTime)
        endTime.setText(task.endTime)
        description.setText(task.description)

        if(task.priority == "Low Priority"){
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.green))
        } else if(task.priority == "Medium Priority"){
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.yellow))
        } else if(task.priority == "High Priority"){
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.red))
        } else if(task.priority == "Max Priority"){
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.purple))
        }

        for (tag in task.tags) {
            addTag(tag)
        }
        for (subtask in task.subTasks) {
            addNewEditText(subtask)
        }
    }

    private fun initListeners(task: Task){

        date.setOnClickListener {
            showDatePickerDialog(date)
        }

        startTime.setOnClickListener {
            showTimePickerDialog(startTime)
        }

        endTime.setOnClickListener {
            showTimePickerDialog(endTime)
        }

        val createTagDialog = CreateTagDialog(mAlertDialog.context)
        btnAddTag.setOnClickListener {
            createTagDialog.showTagInputDialog { tagName ->
                addTag(tagName)
            }
        }

        btnLowPriority.setOnClickListener {
            task.priority = "Low Priority"
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.green))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_yellow))
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_red))
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_purple))
        }

        btnMediumPriority.setOnClickListener {
            task.priority = "Medium Priority"
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.yellow))
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_green))
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_red))
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_purple))
        }

        btnHighPriority.setOnClickListener {
            task.priority = "High Priority"
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.red))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_yellow))
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_green))
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_purple))
        }

        btnMaxPriority.setOnClickListener {
            task.priority = "Max Priority"
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.purple))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_yellow))
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_red))
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.light_green))
        }

        textCreateSubTask.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (!textCreateSubTask.text.isEmpty()) {
                    addNewEditText(textCreateSubTask.text.toString())
                    return@setOnEditorActionListener true
                }
                false
            }
            false
        }

        btnEditTask.setOnClickListener {
            Toast.makeText(mAlertDialog.context, task.priority, Toast.LENGTH_SHORT).show()
        }
    }

    //region Metodos de los Dialogs Time y Date
    private fun showTimePickerDialog(textTimeView: EditText) {
        val timePicker = TimePickerDialogFragment { hour, minute, textView ->
            onTimeSelected(
                hour,
                minute,
                textView
            )
        }
        timePicker.setTextTimeView(textTimeView)
        timePicker.show(fragmentManager, "timePicker")
    }

    private fun onTimeSelected(hour: Int, minutes: Int, textTimeView: EditText) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minutes)

        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = dateFormat.format(calendar.time)

        textTimeView.setText(formattedTime)
    }

    private fun showDatePickerDialog(textDateView: EditText) {
        val datePicker = DatePickerDialogFragment { year, month, day, textView ->
            onDateSelected(
                year,
                month,
                day,
                textView
            )
        }
        datePicker.setTextTimeView(textDateView)
        datePicker.show(fragmentManager, "timePicker")
    }

    private fun onDateSelected(year: Int, month: Int, day: Int, textTimeView: EditText) {
        val formattedDate =
            String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year)
        textTimeView.setText(formattedDate)
    }
    //endregion

    private fun addTag(tagName: String) {
        val newTag = createNewTag(tagName)
        linearTags.removeView(btnAddTag)
        newTagsList.add(newTag)
        linearTags.addView(newTag)
        linearTags.addView(btnAddTag)

        setClickListenersForTags()
    }

    private fun createNewTag(tagName: String): TextView {
        val newTag = TextView(mAlertDialog.context)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val marginRight =
            mAlertDialog.context.resources.getDimensionPixelSize(R.dimen.createTag_margin_right)
        params.setMargins(0, 0, marginRight, 0)

        // Configuración del TextView
        newTag.layoutParams = params
        newTag.text = tagName
        newTag.setBackgroundResource(R.drawable.bc_tags)
        newTag.setTypeface(null, Typeface.BOLD)
        newTag.setTextColor(ContextCompat.getColor(mAlertDialog.context, R.color.white))
        newTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)

        newTag.setPadding(
            mAlertDialog.context.resources.getDimensionPixelSize(R.dimen.createTag_padding_left),
            mAlertDialog.context.resources.getDimensionPixelSize(R.dimen.createTag_padding_top),
            mAlertDialog.context.resources.getDimensionPixelSize(R.dimen.createTag_padding_right),
            mAlertDialog.context.resources.getDimensionPixelSize(R.dimen.createTag_padding_bottom)
        )

        return newTag
    }

    private fun addNewEditText(predefinedText: String? = null) {
        val newEditText = createNewEditText(predefinedText)
        newEditTextList.add(newEditText)
        linearSubTasks.addView(newEditText)

        textCreateSubTask.text.clear()
    }

    private fun createNewEditText(predefinedText: String? = null): EditText {
        val newEditText = EditText(mAlertDialog.context)
        newEditText.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newEditText.inputType = InputType.TYPE_CLASS_TEXT
        newEditText.textSize = 15f
        newEditText.imeOptions = EditorInfo.IME_ACTION_DONE

        // Verifica si se proporcionó un texto predefinido y lo establece en el EditText
        predefinedText?.let {
            newEditText.setText(it)
        }

        newEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && newEditText.text.isBlank()) {
                linearSubTasks.post {
                    removeEditText(newEditText)
                }
            }
        }

        newEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (newEditText.text.isEmpty()) {
                    linearSubTasks.post {
                        removeEditText(newEditText)
                    }
                }
                return@setOnEditorActionListener true
            }
            false
        }

        return newEditText
    }

    private fun removeEditText(editText: EditText) {
        linearSubTasks.removeView(editText)
        newEditTextList.remove(editText)
    }

    private fun setClickListenersForTags() {
        for (tag in newTagsList) {
            tag.setOnClickListener {
                Toast.makeText(
                    mAlertDialog.context,
                    "Etiqueta eliminada: ${tag.text}",
                    Toast.LENGTH_SHORT
                ).show()
                newTagsList.remove(tag)
                linearTags.removeView(tag)
            }
        }
    }
}