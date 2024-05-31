package com.aristidevs.syncotask.objects

data class Notes(
    var noteId: String? = null,
    var title: String,
    var description: String,
    var date: String,
) {
    constructor() : this(null, "", "", "")
}