package com.aristidevs.syncotask.objects

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NoteProvider {

    var listNotes = mutableListOf<Notes>()
    lateinit var dbRef: DatabaseReference
    private var onDataChangeCallback: (() -> Unit)? = null

    constructor(){
        getNotesData()
    }

    fun setOnDataChangedCallback(callback: () -> Unit) {
        this.onDataChangeCallback = callback
    }

    fun getNotesData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Notes")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listNotes.clear()
                if (snapshot.exists()) {
                    for (noteSnap in snapshot.children) {
                        val noteId = noteSnap.key // Obtener el ID del objeto
                        val noteData = noteSnap.getValue(Notes::class.java)

                        noteData?.noteId = noteId!!
                        if (noteData != null) {
                            listNotes.add(noteData)
                        }
                    }
                }
                onDataChangeCallback?.invoke()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
    }

}




