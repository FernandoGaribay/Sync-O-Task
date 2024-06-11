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
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.objects.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTaskDialog(private val context: Context, private val fragmentManager: FragmentManager) {

    private lateinit var mAlertDialog: AlertDialog
    private lateinit var taskId: String
    private lateinit var title: EditText
    private lateinit var date: EditText
    private lateinit var startTime: EditText
    private lateinit var endTime: EditText
    private lateinit var description: EditText
    private lateinit var linearSubTasks: LinearLayout
    private lateinit var textCreateSubTask: EditText
    private lateinit var linearTags: LinearLayout
    private lateinit var lvlNewPriority: String
    private lateinit var btnAddTag: TextView
    private lateinit var btnLowPriority: RadioButton
    private lateinit var btnMediumPriority: RadioButton
    private lateinit var btnHighPriority: RadioButton
    private lateinit var btnMaxPriority: RadioButton
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

        taskId = task.taskId!!

        title = mDialogView.findViewById<EditText>(R.id.textTitle).also { checkNotNull(it) }
        date = mDialogView.findViewById<EditText>(R.id.textDate).also { checkNotNull(it) }
        startTime = mDialogView.findViewById<EditText>(R.id.textStartTime).also { checkNotNull(it) }
        endTime = mDialogView.findViewById<EditText>(R.id.textEndTime).also { checkNotNull(it) }
        description = mDialogView.findViewById<EditText>(R.id.textDescription).also { checkNotNull(it) }
        linearSubTasks = mDialogView.findViewById<LinearLayout>(R.id.linearSubTasks).also { checkNotNull(it) }
        textCreateSubTask = mDialogView.findViewById<EditText>(R.id.textCreateSubTask).also { checkNotNull(it) }
        linearTags = mDialogView.findViewById<LinearLayout>(R.id.linearTags).also { checkNotNull(it) }
        btnAddTag = mDialogView.findViewById<TextView>(R.id.btnAddTag).also { checkNotNull(it) }
        btnLowPriority = mDialogView.findViewById<RadioButton>(R.id.btnLowPriority).also { checkNotNull(it) }
        btnMediumPriority = mDialogView.findViewById<RadioButton>(R.id.btnMediumPriority).also { checkNotNull(it) }
        btnHighPriority = mDialogView.findViewById<RadioButton>(R.id.btnHighPriority).also { checkNotNull(it) }
        btnMaxPriority = mDialogView.findViewById<RadioButton>(R.id.btnMaxPriority).also { checkNotNull(it) }
        btnEditTask = mDialogView.findViewById<Button>(R.id.btnCreateNote).also { checkNotNull(it) }
        btnDeleteTask = mDialogView.findViewById<Button>(R.id.btnDeleteTask).also { checkNotNull(it) }

        initTextViews(task)
    }

    private fun initTextViews(task: Task) {
        title.setText(task.title)
        date.setText(task.date)
        startTime.setText(task.startTime)
        endTime.setText(task.endTime)
        description.setText(task.description)

        if (task.priority == "Low Priority") {
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.green))
            lvlNewPriority = "Low Priority"
        } else if (task.priority == "Medium Priority") {
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.yellow))
            lvlNewPriority = "Medium Priority"
        } else if (task.priority == "High Priority") {
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.red))
            lvlNewPriority = "High Priority"
        } else if (task.priority == "Max Priority") {
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.purple))
            lvlNewPriority = "Max Priority"
        }

        for (tag in task.tags) {
            addTag(tag)
        }
        for (subtask in task.subTasks) {
            addNewEditText(subtask)
        }
    }

    private fun initListeners(task: Task) {

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
            lvlNewPriority = "Low Priority"
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.green))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_yellow
                    )
                )
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_red
                    )
                )
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_purple
                    )
                )
        }

        btnMediumPriority.setOnClickListener {
            lvlNewPriority = "Medium Priority"
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.yellow))
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_green
                    )
                )
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_red
                    )
                )
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_purple
                    )
                )
        }

        btnHighPriority.setOnClickListener {
            lvlNewPriority = "High Priority"
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.red))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_yellow
                    )
                )
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_green
                    )
                )
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_purple
                    )
                )
        }

        btnMaxPriority.setOnClickListener {
            lvlNewPriority = "Max Priority"
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(mAlertDialog.context, R.color.purple))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_yellow
                    )
                )
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_red
                    )
                )
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        mAlertDialog.context,
                        R.color.light_green
                    )
                )
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

            val subTasks = newEditTextList.map { it.text.toString() }.toMutableList()
            val tags = newTagsList.map { it.text.toString() }.toMutableList()
            val task = Task(
                null,
                title.text.toString(),
                date.text.toString(),
                startTime.text.toString(),
                endTime.text.toString(),
                description.text.toString(),
                subTasks,
                tags,
                lvlNewPriority,
                state = false
            )

            if (title.text.isEmpty()) {
                showToast("Por favor, ingrese un título.")
            } else if (task.date.isEmpty()) {
                showToast("Por favor, ingrese una fecha.")
            } else if (task.startTime.isEmpty()) {
                showToast("Por favor, ingrese una hora de inicio.")
            } else if (task.endTime.isEmpty()) {
                showToast("Por favor, ingrese una hora de finalización.")
            } else {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val uid = currentUser?.uid
                    val dbRef =
                        FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser.uid)
                            .child(taskId)
                    dbRef.setValue(task).addOnCompleteListener { taskResult ->
                        if (taskResult.isSuccessful) {
                            showToast("Tarea: " + task.title + " actualizada.")
                        } else {
                            // Manejar el error
                            showToast("Error al actualizar la tarea: " + taskResult.exception?.message)
                        }
                    }
                }
                mAlertDialog.dismiss()
            }
        }

        btnDeleteTask.setOnClickListener {
            val subTasks = newEditTextList.map { it.text.toString() }.toMutableList()
            val tags = newTagsList.map { it.text.toString() }.toMutableList()
            val task = Task(
                null,
                title.text.toString(),
                date.text.toString(),
                startTime.text.toString(),
                endTime.text.toString(),
                description.text.toString(),
                subTasks,
                tags,
                lvlNewPriority,
                state = false
            )
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val uid = currentUser?.uid

                val dbRef =
                    FirebaseDatabase.getInstance().getReference("Tasks").child(currentUser.uid)
                        .child(taskId)
                val taskDeleted = dbRef.removeValue()
                taskDeleted.addOnSuccessListener {
                    showToast("Tarea: \"" + task.title + "\" eliminada.")
                }.addOnFailureListener { error ->
                    showToast("Hubo un error al eliminar la tarea: \"" + task.title + "\"")
                }
                mAlertDialog.dismiss()
            }
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

    private fun showToast(message: String) {
        Toast.makeText(mAlertDialog.context, message, Toast.LENGTH_SHORT).show()
    }
}