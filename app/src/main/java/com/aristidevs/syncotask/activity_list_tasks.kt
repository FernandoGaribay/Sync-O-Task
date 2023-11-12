package com.aristidevs.syncotask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.adapters.listTasksAdapter
import com.aristidevs.syncotask.objects.TaskProvider

class activity_list_tasks : AppCompatActivity() {

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
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = listTasksAdapter(TaskProvider.listTasks)
    }
}