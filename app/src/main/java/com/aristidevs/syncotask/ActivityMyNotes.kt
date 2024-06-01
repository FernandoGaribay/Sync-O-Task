package com.aristidevs.syncotask

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.activity.DashboardActivity
import com.aristidevs.syncotask.adapters.myNotesAdapter
import com.aristidevs.syncotask.interfaces.onNoteClickListener
import com.aristidevs.syncotask.objects.NoteProvider
import com.aristidevs.syncotask.objects.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityMyNotes : AppCompatActivity(), onNoteClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesProvider: NoteProvider
    private lateinit var adapter: myNotesAdapter

    private lateinit var btnMyTasks: LinearLayout
    private lateinit var btnMyNotes: LinearLayout
    private lateinit var btnPomodoro: LinearLayout
    private lateinit var btnCalendar: LinearLayout
    private lateinit var btnCreateNote: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)

        initViews()
        setClickListeners()
    }

    private fun initViews() {
        notesProvider = NoteProvider()

        btnMyTasks = findViewById(R.id.dashboard_btnMyTasks)
        btnMyNotes = findViewById(R.id.dashboard_btnMyNotes)
        btnPomodoro = findViewById(R.id.dashboard_btnPomodoro)
        btnCalendar = findViewById(R.id.dashboard_btnCalendar)
        btnCreateNote = findViewById(R.id.btnCreateNote)

        recyclerView = findViewById(R.id.savedNotes)
    }

    private fun setClickListeners() {
        btnMyTasks.setOnClickListener {
            finish() // Cierra el activity para que el usuario no pueda volver atrás
        }
        btnMyNotes.setOnClickListener {

        }
        btnPomodoro.setOnClickListener {
            val intent = Intent(this, ActivityMyPomodoro::class.java)
            startActivity(intent)
            finish() // Cierra el activity para que el usuario no pueda volver atrás
        }
        btnCalendar.setOnClickListener {

        }
        btnCreateNote.setOnClickListener {
            val intent = Intent(this, activity_createNote::class.java)
            startActivity(intent)
        }
        notesProvider.setOnDataChangedCallback {
            // Filtra las tareas que coinciden con las restricciones
            var filteredNotes = notesProvider.listNotes.toMutableList()

            // Inicializa el adaptador y configura el RecyclerView
            updateInProccess(filteredNotes)
        }
    }

    private fun updateInProccess(filteredTasksDate: List<Note>) {
        if (filteredTasksDate.isEmpty()) {
            adapter = myNotesAdapter(emptyList())
        } else {
            adapter = myNotesAdapter(filteredTasksDate)
            adapter.setOnNoteClickListener(this)
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }
    override fun onItemClick(note: Note) {
        showToast("Título: ${note.title}")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}