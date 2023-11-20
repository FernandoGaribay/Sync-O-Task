package com.aristidevs.syncotask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.adapters.listTasksAdapter
import com.aristidevs.syncotask.interfaces.onTaskClickListener
import com.aristidevs.syncotask.objects.Task
import com.aristidevs.syncotask.objects.TaskProvider

class activity_list_tasks : AppCompatActivity(), onTaskClickListener {

    lateinit var btnBack : ImageView
    private lateinit var strPriority: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskProvider: TaskProvider
    private lateinit var adapter: listTasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tasks)

        initViews()
        initRecyclerView(strPriority)
        setClickListeners()
    }

    private fun initViews() {
        btnBack = findViewById<ImageView>(R.id.btnBack)
        strPriority = intent.getStringExtra("priority")!!
    }

    private fun setClickListeners() {
        btnBack.setOnClickListener { finish() }
    }

    private fun initRecyclerView(priorityFilter: String) {
        recyclerView = findViewById(R.id.listTasksRecycler)
        taskProvider = TaskProvider()

        // Filtra las tareas que coinciden con la prioridad actual
        val filteredTasks = getFilteredTasks(priorityFilter)

        // Inicializa el adaptador y configura el RecyclerView
        adapter = listTasksAdapter(filteredTasks)
        adapter.setOnTaskClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        // Registra el callback para actualizar el adaptador cuando los datos cambien
        taskProvider.setOnDataChangedCallback {
            val updatedFilteredTasks = getFilteredTasks(priorityFilter)
            adapter.updateData(updatedFilteredTasks)
        }
    }

    private fun getFilteredTasks(priorityFilter: String): List<Task> {
        return taskProvider.listTasks.filter { it.priority == priorityFilter }
    }

    override fun onItemClick(task: Task) {
        Toast.makeText(this, "Título: ${task.title}", Toast.LENGTH_SHORT).show()
    }
}