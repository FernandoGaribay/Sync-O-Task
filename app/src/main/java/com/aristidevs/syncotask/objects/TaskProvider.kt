package com.aristidevs.syncotask.objects

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaskProvider {

    var listTasks = mutableListOf<Task>()
    lateinit var dbRef: DatabaseReference
    private var onDataChangeCallback: (() -> Unit)? = null

    constructor(){
        getTasksData()
    }

    fun setOnDataChangedCallback(callback: () -> Unit) {
        this.onDataChangeCallback = callback
    }

    fun getTasksData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Tasks")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listTasks.clear()
                if (snapshot.exists()) {
                    for (taskSnap in snapshot.children) {
                        val taskData = taskSnap.getValue(Task::class.java)
                        listTasks.add(taskData!!)
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




