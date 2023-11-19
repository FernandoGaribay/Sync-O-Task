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
                // Manejar errores aquí
            }
        })
    }

}



/*companion object{
    val listTasks = mutableListOf<Task>() // Crear una lista mutable de tareas

    init {  // Agregar objetos Task a la lista listTasks
        listTasks.addAll(
            listOf(
                Task(
                    title = "Tarea 1",
                    date = "23/Noviembre/2023",
                    startTime = "09:30",
                    endTime = "10:00",
                    description = "Terminar la UI del proyecto de Desarrollo mobil, cumpliendo con los requisitos especificados en el PDF y teniéndola lista a más tardar antes del 10 de noviembre para empezar el Back.",
                    subTasks = mutableListOf("Diseño Conceptual", "Diseño en XML"),
                    tags = mutableListOf("CETI", "ANDROID"),
                    priority = "High Priority"
                ),
                Task(
                    title = "Tarea 2",
                    date = "23/Noviembre/2023",
                    startTime = "09:30",
                    endTime = "10:00",
                    description = "Terminar la UI del proyecto de Desarrollo mobil, cumpliendo con los requisitos especificados en el PDF y teniéndola lista a más tardar antes del 10 de noviembre para empezar el Back.",
                    subTasks = mutableListOf("Diseño Conceptual", "Diseño en XML"),
                    tags = mutableListOf("CETI", "ANDROID"),
                    priority = "High Priority"
                ),
                Task(
                    title = "Tarea 3",
                    date = "23/Noviembre/2023",
                    startTime = "09:30",
                    endTime = "10:00",
                    description = "Terminar la UI del proyecto de Desarrollo mobil, cumpliendo con los requisitos especificados en el PDF y teniéndola lista a más tardar antes del 10 de noviembre para empezar el Back.",
                    subTasks = mutableListOf("Diseño Conceptual", "Diseño en XML"),
                    tags = mutableListOf("CETI", "ANDROID"),
                    priority = "High Priority"
                ),
                Task(
                    title = "Tarea 4",
                    date = "23/Noviembre/2023",
                    startTime = "09:30",
                    endTime = "10:00",
                    description = "Terminar la UI del proyecto de Desarrollo mobil, cumpliendo con los requisitos especificados en el PDF y teniéndola lista a más tardar antes del 10 de noviembre para empezar el Back.",
                    subTasks = mutableListOf("Diseño Conceptual", "Diseño en XML"),
                    tags = mutableListOf("CETI", "ANDROID"),
                    priority = "High Priority"
                ),
                Task(
                    title = "Tarea 5",
                    date = "23/Noviembre/2023",
                    startTime = "09:30",
                    endTime = "10:00",
                    description = "Terminar la UI del proyecto de Desarrollo mobil, cumpliendo con los requisitos especificados en el PDF y teniéndola lista a más tardar antes del 10 de noviembre para empezar el Back.",
                    subTasks = mutableListOf("Diseño Conceptual", "Diseño en XML"),
                    tags = mutableListOf("CETI", "ANDROID"),
                    priority = "High Priority"
                ),
                Task(
                    title = "Tarea 6",
                    date = "23/Noviembre/2023",
                    startTime = "09:30",
                    endTime = "10:00",
                    description = "Terminar la UI del proyecto de Desarrollo mobil, cumpliendo con los requisitos especificados en el PDF y teniéndola lista a más tardar antes del 10 de noviembre para empezar el Back.",
                    subTasks = mutableListOf("Diseño Conceptual", "Diseño en XML"),
                    tags = mutableListOf("CETI", "ANDROID"),
                    priority = "High Priority"
                ),
                Task(
                    title = "Tarea 7",
                    date = "23/Noviembre/2023",
                    startTime = "09:30",
                    endTime = "10:00",
                    description = "Terminar la UI del proyecto de Desarrollo mobil, cumpliendo con los requisitos especificados en el PDF y teniéndola lista a más tardar antes del 10 de noviembre para empezar el Back.",
                    subTasks = mutableListOf("Diseño Conceptual", "Diseño en XML"),
                    tags = mutableListOf("CETI", "ANDROID"),
                    priority = "High Priority"
                ),
                Task(
                    title = "Tarea 8",
                    date = "23/Noviembre/2023",
                    startTime = "09:30",
                    endTime = "10:00",
                    description = "Terminar la UI del proyecto de Desarrollo mobil, cumpliendo con los requisitos especificados en el PDF y teniéndola lista a más tardar antes del 10 de noviembre para empezar el Back.",
                    subTasks = mutableListOf("Diseño Conceptual", "Diseño en XML"),
                    tags = mutableListOf("CETI", "ANDROID"),
                    priority = "High Priority"
                )
            )
        )
    }
} */



