package com.aristidevs.syncotask

import android.content.res.ColorStateList
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.aristidevs.syncotask.dialogs.CreateTagDialog
import com.aristidevs.syncotask.dialogs.DatePickerDialogFragment
import com.aristidevs.syncotask.dialogs.TimePickerDialogFragment
import com.aristidevs.syncotask.objects.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class activity_createTask : AppCompatActivity() {

    lateinit var btnBack: ImageView
    lateinit var linearSubTasks: LinearLayout
    lateinit var textCreateSubTask: EditText
    lateinit var linearTags: LinearLayout
    lateinit var btnAddTag: TextView
    lateinit var btnCreateTask: Button
    lateinit var lvlPriority: String

    lateinit var textDate: EditText
    lateinit var textStartTime: EditText
    lateinit var textEndTime: EditText
    lateinit var btnLowPriority: LinearLayout
    lateinit var btnMediumPriority: LinearLayout
    lateinit var btnHighPriority: LinearLayout
    lateinit var btnMaxPriority: LinearLayout

    private val newEditTextList = mutableListOf<EditText>()
    private val newTagsList = mutableListOf<TextView>()
    private lateinit var dbRef : DatabaseReference

    init {
        lvlPriority = "Medium Priority"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)


        //region Inicializar metodos
        btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        textDate = findViewById<EditText>(R.id.textDate)
        textDate.setOnClickListener {
            showDatePickerDialog(textDate)
        }

        textStartTime = findViewById<EditText>(R.id.textStartTime)
        textStartTime.setOnClickListener {
            showTimePickerDialog(textStartTime)
        }

        textEndTime = findViewById<EditText>(R.id.textEndTime)
        textEndTime.setOnClickListener {
            showTimePickerDialog(textEndTime)
        }

        linearSubTasks = findViewById(R.id.layoutSubTasks)
        textCreateSubTask = findViewById(R.id.textCreateSubTask)
        textCreateSubTask.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (!textCreateSubTask.text.isEmpty()) {
                    addNewEditText()
                    return@setOnEditorActionListener true
                }
                false
            }
            false
        }

        val createTagDialog = CreateTagDialog(this)
        linearTags = findViewById<LinearLayout>(R.id.linearTags)
        btnAddTag = findViewById<TextView>(R.id.btnAddTag)
        btnAddTag.setOnClickListener {
            createTagDialog.showTagInputDialog { tagName ->
                addTag(tagName)
            }
        }

        btnLowPriority = findViewById<LinearLayout>(R.id.btnLowPriority)
        btnLowPriority.setOnClickListener {
            lvlPriority = "Low Priority"
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_yellow))
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_red))
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_purple))
        }

        btnMediumPriority = findViewById<LinearLayout>(R.id.btnMediumPriority)
        btnMediumPriority.setOnClickListener {
            lvlPriority = "Medium Priority"
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow))
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_green))
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_red))
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_purple))
        }

        btnHighPriority = findViewById<LinearLayout>(R.id.btnHighPriority)
        btnHighPriority.setOnClickListener {
            lvlPriority = "High Priority"
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_yellow))
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_green))
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_purple))
        }

        btnMaxPriority = findViewById<LinearLayout>(R.id.btnMaxPriority)
        btnMaxPriority.setOnClickListener {
            lvlPriority = "Max Priority"
            btnMaxPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple))
            btnMediumPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_yellow))
            btnHighPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_red))
            btnLowPriority.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_green))
        }

        btnCreateTask = findViewById<Button>(R.id.btnCreateTask)
        btnCreateTask.setOnClickListener {
            val subTasks = newEditTextList.map { it.text.toString() }.toMutableList()
            val tags = newTagsList.map { it.text.toString() }.toMutableList()
            val task = Task(
                title = findViewById<EditText>(R.id.textTitle).text.toString(),
                date = findViewById<EditText>(R.id.textDate).text.toString(),
                startTime = findViewById<EditText>(R.id.textStartTime).text.toString(),
                endTime = findViewById<EditText>(R.id.textEndTime).text.toString(),
                description = findViewById<EditText>(R.id.textDescription).text.toString(),
                subTasks = subTasks,
                tags = tags,
                priority = lvlPriority,
                state = false
            )

            if (title.isEmpty()) {
                showToast("Por favor, ingrese un título.")
            } else if (task.date.isEmpty()) {
                showToast("Por favor, ingrese una fecha.")
            } else if (task.startTime.isEmpty()) {
                showToast("Por favor, ingrese una hora de inicio.")
            } else if (task.endTime.isEmpty()) {
                showToast("Por favor, ingrese una hora de finalización.")
            } else {
                dbRef = FirebaseDatabase.getInstance().getReference("Tasks")
                val empId = dbRef.push().key!!
                dbRef.child(empId).setValue(task)
                    .addOnCompleteListener{
                        Toast.makeText(this,"Datos subidos", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{ err ->
                        Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
                    }
                finish()
            }
        }
        //endregion
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
        timePicker.show(supportFragmentManager, "timePicker")
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
        datePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onDateSelected(year: Int, month: Int, day: Int, textTimeView: EditText) {
        val formattedDate =
            String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year)
        textTimeView.setText(formattedDate)
    }
    //endregion

    //region Metodos para añadir SubTarea
    private fun addNewEditText() {
        val newEditText = createNewEditText()
        newEditTextList.add(newEditText)
        linearSubTasks.addView(newEditText)

        textCreateSubTask.text.clear()
    }

    private fun createNewEditText(): EditText {
        val newEditText = EditText(this)
        newEditText.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newEditText.inputType = InputType.TYPE_CLASS_TEXT
        newEditText.textSize = 15f
        newEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        newEditText.setText(textCreateSubTask.text.toString())

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
    //endregion

    //region Metodos para añadir Tags
    private fun setClickListenersForTags() {
        for (tag in newTagsList) {
            tag.setOnClickListener {
                Toast.makeText(this, "Etiqueta eliminada: ${tag.text}", Toast.LENGTH_SHORT).show()
                newTagsList.remove(tag)
                linearTags.removeView(tag)
            }
        }
    }

    private fun addTag(tagName: String) {
        val newTag = createNewTag(tagName)
        linearTags.removeView(btnAddTag)
        newTagsList.add(newTag)
        linearTags.addView(newTag)
        linearTags.addView(btnAddTag)

        setClickListenersForTags()
    }

    private fun createNewTag(tagName: String): TextView {
        val newTag = TextView(this)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val marginRight = resources.getDimensionPixelSize(R.dimen.tag_margin_right)
        params.setMargins(0, 0, marginRight, 0)

        // Configuración del TextView
        newTag.layoutParams = params
        newTag.text = tagName
        newTag.setBackgroundResource(R.drawable.bc_tags)
        newTag.setTypeface(null, Typeface.BOLD)
        newTag.setTextColor(ContextCompat.getColor(this, R.color.white))
        newTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)

        newTag.setPadding(
            resources.getDimensionPixelSize(R.dimen.tag_padding_left),
            resources.getDimensionPixelSize(R.dimen.tag_padding_top),
            resources.getDimensionPixelSize(R.dimen.tag_padding_right),
            resources.getDimensionPixelSize(R.dimen.tag_padding_bottom)
        )

        return newTag
    }


    //endregion

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}