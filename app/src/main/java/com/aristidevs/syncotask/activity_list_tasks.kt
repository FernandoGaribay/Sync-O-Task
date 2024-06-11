package com.aristidevs.syncotask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.adapters.listTasksAdapter
import com.aristidevs.syncotask.dialogs.CreateTagDialog
import com.aristidevs.syncotask.dialogs.EditTaskDialog
import com.aristidevs.syncotask.interfaces.onTaskClickListener
import com.aristidevs.syncotask.objects.Task
import com.aristidevs.syncotask.objects.TaskProvider
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class activity_list_tasks : AppCompatActivity(), onTaskClickListener {

    lateinit var btnBack : ImageView
    private lateinit var strPriority: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskProvider: TaskProvider
    private lateinit var adapter: listTasksAdapter

    private lateinit var textOverdue : TextView
    private lateinit var textToDo : TextView
    private lateinit var textOpen : TextView
    private lateinit var textDueToday : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tasks)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: ""
        val strPriority = intent.getStringExtra("priority")!!

        initViews()
        initRecyclerView(uid, strPriority)
        setClickListeners()
        taskProvider.getTasksData()
    }

    private fun initViews() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid ?: ""
        taskProvider = TaskProvider(uid)

        btnBack = findViewById(R.id.btnBack)
        strPriority = intent.getStringExtra("priority")!!

        textOverdue = findViewById(R.id.textOverdue)
        textToDo = findViewById(R.id.textToDo)
        textOpen = findViewById(R.id.textOpen)
        textDueToday = findViewById(R.id.textDueToday)
    }

    private fun setClickListeners() {
        btnBack.setOnClickListener { finish() }

        // Registra el callback para actualizar el adaptador cuando los datos cambien
        taskProvider.setOnDataChangedCallback {
            val updatedFilteredTasks = getFilteredTasks(strPriority)
            adapter.updateData(updatedFilteredTasks)

            textOverdue.text = taskProvider.listTasks.count { isOverdueDate(it.date) == 1 && it.priority == strPriority }.toString()
            textToDo.text = taskProvider.listTasks.count { isToDoDate(it.date) == 1 && it.priority == strPriority }.toString()
            textOpen.text = taskProvider.listTasks.count { isOpen(it.date) == 1 && it.priority == strPriority }.toString()
            textDueToday.text = taskProvider.listTasks.count { isDueToday(it.date) == 1 && it.priority == strPriority }.toString()
        }
    }

    private fun initRecyclerView(uid: String, priorityFilter: String) {
        recyclerView = findViewById(R.id.listTasksRecycler)

        // Filtra las tareas que coinciden con la prioridad actual
        val filteredTasks = getFilteredTasks(priorityFilter)

        // Inicializa el adaptador y configura el RecyclerView
        adapter = listTasksAdapter(filteredTasks)
        adapter.setOnTaskClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun getFilteredTasks(priorityFilter: String): List<Task> {
        return taskProvider.listTasks.filter { it.priority == priorityFilter }
    }

    override fun onItemClick(task: Task) {
        val editTaskDialog = EditTaskDialog(this, supportFragmentManager)
        editTaskDialog.showEditTaskDialog(task)
    }

    //region Metodos filtrado de fechas
    private fun isOverdueDate(taskDate: String): Int {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdf.parse(taskDate)
        val currentDateObj = sdf.parse(getCurrentDate())
        return if (date.before(currentDateObj)) {
            1
        } else {
            0
        }
    }

    private fun isToDoDate(taskDate: String): Int {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdf.parse(taskDate)
        val currentDateObj = sdf.parse(getCurrentDate())

        return if (date.after(currentDateObj) || date.equals(currentDateObj)) {
            1
        } else {
            0
        }
    }

    private fun isOpen(taskDate: String): Int {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdf.parse(taskDate)
        val currentDate = Date()

        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val sevenDaysLater = calendar.time

        return if (date.after(currentDate) && date.before(sevenDaysLater)) {
            1
        } else {
            0
        }
    }

    private fun isDueToday(taskDate: String): Int {
        return if (taskDate == getCurrentDate()) 1 else 0
    }

    private fun getFilteredTasksByDate(date: String): List<Task> {
        return taskProvider.listTasks.filter { it.date == date }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    //endregion

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

