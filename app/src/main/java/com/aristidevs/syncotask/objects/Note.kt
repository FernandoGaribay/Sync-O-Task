package com.aristidevs.syncotask.objects

data class Note(
    var noteId: String? = null,
    var title: String,
    var description: String,
    var date: String,
    var imageUrl: String? = null
) {
    constructor() : this(null, "", "", "")
}