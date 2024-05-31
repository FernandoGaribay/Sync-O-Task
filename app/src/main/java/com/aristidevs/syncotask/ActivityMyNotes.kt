package com.aristidevs.syncotask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.adapters.myNotesAdapter
import com.aristidevs.syncotask.interfaces.onNoteClickListener
import com.aristidevs.syncotask.objects.NoteProvider
import com.aristidevs.syncotask.objects.Notes

class ActivityMyNotes : AppCompatActivity(), onNoteClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesProvider: NoteProvider
    private lateinit var adapter: myNotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_notes)

        initViews()
        setClickListeners()
    }

    private fun initViews() {
        notesProvider = NoteProvider()

        recyclerView = findViewById(R.id.savedNotes)
    }

    private fun setClickListeners() {
        notesProvider.setOnDataChangedCallback {
            // Filtra las tareas que coinciden con las restricciones
            var filteredNotes = notesProvider.listNotes.toMutableList()

            // Inicializa el adaptador y configura el RecyclerView
            updateInProccess(filteredNotes)
        }
    }

    private fun updateInProccess(filteredTasksDate: List<Notes>) {
        if (filteredTasksDate.isEmpty()) {
            adapter = myNotesAdapter(emptyList())
        } else {
            adapter = myNotesAdapter(filteredTasksDate)
            adapter.setOnNoteClickListener(this)
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }
    override fun onItemClick(note: Notes) {

    }
}