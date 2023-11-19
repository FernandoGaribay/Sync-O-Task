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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tasks)
        initRecyclerView()

        btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.listTasksRecycler)
        val taskProvider = TaskProvider()
        val adapter = listTasksAdapter(taskProvider.listTasks)

        adapter.setOnTaskClickListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        // Registra el callback para actualizar el adaptador cuando los datos cambien
        taskProvider.setOnDataChangedCallback {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(task: Task) {
        Toast.makeText(this, "Título: ${task.title}", Toast.LENGTH_SHORT).show()
    }
}