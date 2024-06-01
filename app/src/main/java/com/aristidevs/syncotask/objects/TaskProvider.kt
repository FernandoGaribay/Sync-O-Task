package com.aristidevs.syncotask.objects

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaskProvider(private val uid: String) {

    var listTasks = mutableListOf<Task>()
    private lateinit var dbRef: DatabaseReference
    private var onDataChangeCallback: (() -> Unit)? = null

    init {
        // Cargar las tareas del usuario autenticado
        loadTasks()
    }

    fun setOnDataChangedCallback(callback: () -> Unit) {
        this.onDataChangeCallback = callback
    }

    private fun loadTasks() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        dbRef = FirebaseDatabase.getInstance().getReference("Tasks").child(uid)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTasks.clear()
                if (snapshot.exists()) {
                    for (taskSnap in snapshot.children) {
                        val task = taskSnap.getValue(Task::class.java)
                        task?.let { listTasks.add(it) }
                    }
                }
                onDataChangeCallback?.invoke()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
    }

    fun getTasksData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Tasks").child(uid)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTasks.clear()
                if (snapshot.exists()) {
                    for (taskSnap in snapshot.children) {
                        val taskId = taskSnap.key // Obtener el ID del objeto
                        val taskData = taskSnap.getValue(Task::class.java)

                        taskData?.taskId = taskId!!
                        if (taskData != null) {
                            listTasks.add(taskData)
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