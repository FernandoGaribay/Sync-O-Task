package com.aristidevs.syncotask.activity

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.adapters.CustomAdapter
import com.aristidevs.syncotask.objects.TaskProvider


class dashboard : AppCompatActivity() {

    lateinit var btnMyTasks: LinearLayout
    lateinit var btnMyNotes: LinearLayout
    lateinit var btnPomodoro: LinearLayout
    lateinit var btnCalendar: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initRecyclerView()

        btnMyTasks = findViewById<LinearLayout>(R.id.dashboard_btnMyTasks)
        btnMyTasks.setOnClickListener{
            Toast.makeText(this, "My Tasks", Toast.LENGTH_SHORT).show()
        }

        btnMyNotes = findViewById<LinearLayout>(R.id.dashboard_btnMyNotes)
        btnMyNotes.setOnClickListener{
            Toast.makeText(this, "My Notes", Toast.LENGTH_SHORT).show()
        }

        btnPomodoro = findViewById<LinearLayout>(R.id.dashboard_btnPomodoro)
        btnPomodoro.setOnClickListener{
            Toast.makeText(this, "Boton Pomodoro", Toast.LENGTH_SHORT).show()
        }

        btnCalendar = findViewById<LinearLayout>(R.id.dashboard_btnCalendar)
        btnCalendar.setOnClickListener{
            Toast.makeText(this, "Boton Calendar", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.inProgressRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CustomAdapter(TaskProvider.listTasks)
    }
}