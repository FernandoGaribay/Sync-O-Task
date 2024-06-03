package com.aristidevs.syncotask

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import me.tankery.lib.circularseekbar.CircularSeekBar
import java.lang.Exception

class ActivityMyPomodoro : AppCompatActivity(), onTaskClickListener {

    private lateinit var circularSeekBar: CircularSeekBar
    private lateinit var sessions: TextView
    private lateinit var time: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskProvider: TaskProvider
    private lateinit var adapter: dashboardAdapter

    private lateinit var btnMyTasks: LinearLayout
    private lateinit var btnMyNotes: LinearLayout
    private lateinit var btnPomodoro: LinearLayout
    private lateinit var btnCalendar: LinearLayout

    private lateinit var tasks: MutableList<Task>

    private val workTimeMinutes = 1
    private val breakTimeMinutes = 1
    private var sessionCount = 0
    private var isBreak = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pomodoro)

        initViews()
        setClickListeners()
        startPomodoroSession()
    }

    private fun initViews() {
        // Inicializar el proveedor de tareas
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: ""
        taskProvider = TaskProvider(uid)

        circularSeekBar = findViewById(R.id.circularSeekBar)
        sessions = findViewById(R.id.sessions)
        time = findViewById(R.id.time)

        btnMyTasks = findViewById(R.id.dashboard_btnMyTasks)
        btnMyNotes = findViewById(R.id.dashboard_btnMyNotes)
        btnPomodoro = findViewById(R.id.dashboard_btnPomodoro)
        btnCalendar = findViewById(R.id.dashboard_btnCalendar)

        recyclerView = findViewById(R.id.recyclerPomodoroTasks)
    }

    private fun setClickListeners() {
        btnMyTasks.setOnClickListener {
            finish()
        }
        btnMyNotes.setOnClickListener {
            val intent = Intent(this, ActivityMyNotes::class.java)
            startActivity(intent)
            finish()
        }
        btnPomodoro.setOnClickListener {
            showToast("My Pomodoro")
        }
        btnCalendar.setOnClickListener {
            showToast("My Calendar")
        }

        taskProvider.setOnDataChangedCallback {
            var filteredTasks = taskProvider.listTasks.toMutableList()

            tasks = taskProvider.listTasks.filter { it.state == false || it.state == true }
                .toMutableList()
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

    private fun startPomodoroSession() {
        var workTimeInMillis = workTimeMinutes * 60 * 1000L
        circularSeekBar.max = workTimeMinutes * 60f
        circularSeekBar.progress = 0f

        val timer = object : CountDownTimer(workTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val elapsedSeconds = (workTimeInMillis - millisUntilFinished) / 1000
                val minutes = elapsedSeconds / 60
                val seconds = (elapsedSeconds % 60).toInt()
                time.text = String.format("%02d:%02d", minutes, seconds)
                circularSeekBar.progress = elapsedSeconds.toFloat()
            }

            override fun onFinish() {
                if (!isBreak) {
                    showToast("Break Time!")
                    isBreak = true
                    circularSeekBar.max = breakTimeMinutes * 60f
                    startBreak()
                } else {
                    sessionCount++
                    sessions.text = "Sessions: $sessionCount"
                    isBreak = false
                    if (sessionCount < 4) {
                        showToast("New Pomodoro Session")
                        circularSeekBar.max = workTimeMinutes * 60f
                        startPomodoroSession()
                    } else {
                        showToast("Pomodoro Completed")
                    }
                }
            }

        }
        timer.start()
    }

    private fun startBreak() {
        var breakTimeInMillis = breakTimeMinutes * 60 * 1000L
        val initialBreakTimeInMillis = breakTimeInMillis
        circularSeekBar.progress = circularSeekBar.max

        val timer = object : CountDownTimer(breakTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val elapsedSeconds = (initialBreakTimeInMillis - millisUntilFinished) / 1000
                val minutes = (breakTimeInMillis - millisUntilFinished) / 1000 / 60
                val seconds = ((breakTimeInMillis - millisUntilFinished) / 1000 % 60).toInt()
                time.text = String.format("%02d:%02d", minutes, seconds)
                circularSeekBar.progress = elapsedSeconds.toFloat()
            }

            override fun onFinish() {
                if (!isBreak) {
                    showToast("Break Time!")
                    isBreak = true
                    circularSeekBar.max = workTimeMinutes * 60f
                    startPomodoroSession()
                } else {
                    sessionCount++
                    sessions.text = "Sessions: $sessionCount"
                    isBreak = false
                    if (sessionCount < 4) {
                        showToast("New Pomodoro Session")
                        circularSeekBar.max = workTimeMinutes * 60f
                        startPomodoroSession()
                    } else {
                        showToast("Pomodoro Completed")
                    }
                }
            }
        }
        timer.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}