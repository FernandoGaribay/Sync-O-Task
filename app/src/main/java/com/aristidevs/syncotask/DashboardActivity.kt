package com.aristidevs.syncotask.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.activity_createTask
import com.aristidevs.syncotask.activity_list_tasks
import com.aristidevs.syncotask.adapters.dashboardAdapter
import com.aristidevs.syncotask.adapters.listTasksAdapter
import com.aristidevs.syncotask.objects.Task
import com.aristidevs.syncotask.objects.TaskProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskProvider: TaskProvider
    private lateinit var adapter: dashboardAdapter

    private lateinit var textMaxPriority: TextView
    private lateinit var textHighPriority: TextView
    private lateinit var textMediumPriority: TextView
    private lateinit var textLowPriority: TextView

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
        btnMyNotes.setOnClickListener { showToast("My Notes") }
        btnPomodoro.setOnClickListener { showToast("Boton Pomodoro") }
        btnCalendar.setOnClickListener { showToast("Boton Calendar") }
        btnCreateTask.setOnClickListener { startActivity(Intent(this, activity_createTask::class.java)) }

        btnMaxPriority.setOnClickListener { startListTasksActivity("Max Priority") }
        btnHighPriority.setOnClickListener { startListTasksActivity("High Priority") }
        btnMediumPriority.setOnClickListener { startListTasksActivity("Medium Priority") }
        btnLowPriority.setOnClickListener { startListTasksActivity("Low Priority") }

        taskProvider.setOnDataChangedCallback {
            // Filtra las tareas que coinciden con la fecha actual
            val filteredTasksDate = getFilteredTasksByDate(getCurrentDate())

            // Filtra las tareas que coinciden con la prioridad
            val filMaxPrio = getFilteredTasksByPriority("Max Priority")
            val filHighPrio = getFilteredTasksByPriority("High Priority")
            val filMediumPrio = getFilteredTasksByPriority("Medium Priority")
            val filLowPrio = getFilteredTasksByPriority("Low Priority")

            // Inicializa el adaptador y configura el RecyclerView
            updateInProccess(filteredTasksDate)
            updateTextsPriority(filMaxPrio, filHighPrio, filMediumPrio, filLowPrio)
        }
    }

    private fun updateInProccess(filteredTasksDate : List<Task>){
        adapter = dashboardAdapter(filteredTasksDate)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun updateTextsPriority(filMaxPrio : Int, filHighPrio : Int, filMediumPrio : Int, filLowPrio : Int){
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

    private fun getFilteredTasksByDate(date: String): List<Task> {
        return taskProvider.listTasks.filter { it.date == date }
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}