package com.aristidevs.syncotask.objects

import java.util.Date

data class Task(
    val title: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val subTasks: MutableList<String> = mutableListOf(),
    val tags: MutableList<String> = mutableListOf(),
    val priority: String
)