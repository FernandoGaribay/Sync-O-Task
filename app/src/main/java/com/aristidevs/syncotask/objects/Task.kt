package com.aristidevs.syncotask.objects

data class Task(
    var taskId: String? = null,
    var title: String,
    var date: String,
    var startTime: String,
    var endTime: String,
    var description: String,
    var subTasks: MutableList<String> = mutableListOf(),
    var tags: MutableList<String> = mutableListOf(),
    var priority: String,
    var state: Boolean
) {
    constructor() : this(null, "", "", "", "", "", mutableListOf(), mutableListOf(), "", false)
}