package com.aristidevs.syncotask.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.ActivityMyNotes
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.activity_createTask
import com.aristidevs.syncotask.activity_list_tasks
import com.aristidevs.syncotask.adapters.dashboardAdapter
import com.aristidevs.syncotask.adapters.listTasksAdapter
import com.aristidevs.syncotask.dialogs.EditTaskDialog
import com.aristidevs.syncotask.interfaces.onTaskClickListener
import com.aristidevs.syncotask.objects.Task
import com.aristidevs.syncotask.objects.TaskProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DashboardActivity : AppCompatActivity(), onTaskClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskProvider: TaskProvider
    private lateinit var adapter: dashboardAdapter

    private lateinit var textMaxPriority: TextView
    private lateinit var textHighPriority: TextView
    private lateinit var textMediumPriority: TextView
    private lateinit var textLowPriority: TextView

    private lateinit var btnMenu: ImageView
    private lateinit var btnMyTasks: LinearLayout
    private lateinit var btnMyNotes: LinearLayout
    private lateinit var btnPomodoro: LinearLayout
    private lateinit var btnCalendar: LinearLayout
    private lateinit var btnCreateTask: FloatingActionButton

    private lateinit var btnMaxPriority: CardView
    private lateinit var btnHighPriority: CardView
    private lateinit var btnMediumPriority: CardView
    private lateinit var btnLowPriority: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initViews()
        setClickListeners() // updateInProccess() y updateTextsPriority()
    }

    private fun initViews() {
        taskProvider = TaskProvider()

        btnMenu = findViewById<ImageView>(R.id.btnMenu)
        textMaxPriority = findViewById<EditText>(R.id.text_maxTasks)
        textHighPriority = findViewById<EditText>(R.id.text_highTasks)
        textMediumPriority = findViewById<EditText>(R.id.text_mediumTasks)
        textLowPriority = findViewById<EditText>(R.id.text_lowTasks)
        recyclerView = findViewById(R.id.inProgressRecycler)
        btnMyTasks = findViewById(R.id.dashboard_btnMyTasks)
        btnMyNotes = findViewById(R.id.dashboard_btnMyNotes)
        btnPomodoro = findViewById(R.id.dashboard_btnPomodoro)
        btnCalendar = findViewById(R.id.dashboard_btnCalendar)
        btnCreateTask = findViewById(R.id.btnCreateTask)
        btnMaxPriority = findViewById(R.id.btn_Tasks_MaxPriority)
        btnHighPriority = findViewById(R.id.btn_Tasks_HighPriority)
        btnMediumPriority = findViewById(R.id.btn_Tasks_MediumPriority)
        btnLowPriority = findViewById(R.id.btn_Tasks_LowPriority)
    }

    private fun setClickListeners() {
        btnMyTasks.setOnClickListener { showToast("My Tasks") }
        btnMyNotes.setOnClickListener {
            val intent = Intent(this, ActivityMyNotes::class.java)
            startActivity(intent)
            finish() // Cierra el activity para que el usuario no pueda volver atrás
        }
        btnPomodoro.setOnClickListener { showToast("Boton Pomodoro") }
        btnCalendar.setOnClickListener { showToast("Boton Calendar") }
        btnCreateTask.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    activity_createTask::class.java
                )
            )
        }

        btnMaxPriority.setOnClickListener { startListTasksActivity("Max Priority") }
        btnHighPriority.setOnClickListener { startListTasksActivity("High Priority") }
        btnMediumPriority.setOnClickListener { startListTasksActivity("Medium Priority") }
        btnLowPriority.setOnClickListener { startListTasksActivity("Low Priority") }

        taskProvider.setOnDataChangedCallback {
            // Filtra las tareas que coinciden con las restricciones
            var filteredTasks = taskProvider.listTasks.toMutableList()
            filteredTasks = getFilteredTasksByDate(filteredTasks, getCurrentDate())
            filteredTasks = getFilteredTasksByState(filteredTasks)

            // Filtra las tareas que coinciden con la prioridad
            val filMaxPrio = getFilteredTasksByPriority("Max Priority")
            val filHighPrio = getFilteredTasksByPriority("High Priority")
            val filMediumPrio = getFilteredTasksByPriority("Medium Priority")
            val filLowPrio = getFilteredTasksByPriority("Low Priority")

            // Inicializa el adaptador y configura el RecyclerView
            updateInProccess(filteredTasks)
            updateTextsPriority(filMaxPrio, filHighPrio, filMediumPrio, filLowPrio)
        }

        btnMenu.setOnClickListener {

        }
    }

    private fun updateInProccess(filteredTasksDate: List<Task>) {
        if (filteredTasksDate.isEmpty()) {
            adapter = dashboardAdapter(emptyList())
        } else {
            adapter = dashboardAdapter(filteredTasksDate)
            adapter.setOnTaskClickListener(this)
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun updateTextsPriority(
        filMaxPrio: Int,
        filHighPrio: Int,
        filMediumPrio: Int,
        filLowPrio: Int
    ) {
        textMaxPriority.setText(filMaxPrio.toString() + " Tasks")
        textHighPriority.setText(filHighPrio.toString() + " Tasks")
        textMediumPriority.setText(filMediumPrio.toString() + " Tasks")
        textLowPriority.setText(filLowPrio.toString() + " Tasks")
    }

    private fun startListTasksActivity(priority: String) {
        val intent = Intent(this, activity_list_tasks::class.java)
        intent.putExtra("priority", priority)
        startActivity(intent)
    }

    private fun getFilteredTasksByDate(listTask: MutableList<Task>, date: String): MutableList<Task> {
        return listTask.filter { it.date == date }.toMutableList()
    }

    private fun getFilteredTasksByState(listTask: MutableList<Task>): MutableList<Task> {
        return listTask.filter { it.state == false }.toMutableList()
    }

    private fun getFilteredTasksByPriority(priorityFilter: String): Int {
        var count: Int = 0
        for (task in taskProvider.listTasks) {
            if (task.priority == priorityFilter) {
                count++
            }
        }

        return count
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onItemClick(task: Task) {
        val popupMenu = PopupMenu(this, recyclerView)

        popupMenu.menuInflater.inflate(R.menu.menu_task_options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuChecked -> {
                    val dbRef =
                        FirebaseDatabase.getInstance().getReference("Tasks").child(task.taskId!!)
                    task.taskId = null
                    task.state = true
                    dbRef.setValue(task).addOnCompleteListener { taskResult ->
                        if (taskResult.isSuccessful) {
                            showToast("Tarea: " + task.title + " terminada.")
                        } else {
                            showToast("Error al actualizar la tarea: " + taskResult.exception?.message)
                        }
                    }
                }

                R.id.menuEdit -> {
                    val editTaskDialog = EditTaskDialog(this, supportFragmentManager)
                    editTaskDialog.showEditTaskDialog(task)
                }
            }
            true
        }
        try {
            val fieldMPopup = popupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Dashboard", "Error showing menu icons. ", e)
        }
        popupMenu.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}