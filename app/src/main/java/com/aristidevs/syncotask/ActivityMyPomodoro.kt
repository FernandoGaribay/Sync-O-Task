package com.aristidevs.syncotask

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.adapters.dashboardAdapter
import com.aristidevs.syncotask.interfaces.onTaskClickListener
import com.aristidevs.syncotask.objects.DatabaseHelper
import com.aristidevs.syncotask.objects.DatabaseManager
import com.aristidevs.syncotask.objects.Task
import com.aristidevs.syncotask.objects.TaskProvider
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class ActivityMyPomodoro : AppCompatActivity(), onTaskClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskProvider: TaskProvider
    private lateinit var adapter: dashboardAdapter

    private lateinit var tasks: MutableList<Task>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pomodoro)
        initViews()
        setClickListeners()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: ""
        taskProvider = TaskProvider(uid)
        taskProvider.getTasksData()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerPomodoroTasks)
    }

    private fun setClickListeners() {
        taskProvider.setOnDataChangedCallback {
            var filteredTasks = taskProvider.listTasks.toMutableList()

            tasks = taskProvider.listTasks.filter { it.state == false || it.state == true }.toMutableList()
            saveFirestoreDataToSQLite()

            // Inicializa el adaptador y configura el RecyclerView
            updateInProccess(filteredTasks)
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

    private fun saveFirestoreDataToSQLite() {
        val db = DatabaseManager.getDatabase()
        db.execSQL("delete from Tasks")
        for (task in tasks) {
            try {
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_TITLE, task.title)
                    put(DatabaseHelper.COLUMN_DATE, task.date)
                    put(DatabaseHelper.COLUMN_DESCRIPTION, task.description)
                    put(DatabaseHelper.COLUMN_START_TIME, task.startTime)
                    put(DatabaseHelper.COLUMN_END_TIME, task.endTime)
                    put(DatabaseHelper.COLUMN_PRIORITY, task.priority)
                    put(DatabaseHelper.COLUMN_STATE, if (task.state) 1 else 0)
                    put(DatabaseHelper.COLUMN_SUB_TASKS, task.subTasks.joinToString(","))
                    put(DatabaseHelper.COLUMN_TAGS, task.tags.joinToString(","))
                }
                db.insert(DatabaseHelper.TABLE_TASKS, null, values)
            } catch (e: Exception) {
                Log.e("Dashboard", "Error al guardar datos en SQLite: ", e)
            }
        }
    }

    override fun onItemClick(task: Task) {

    }
}