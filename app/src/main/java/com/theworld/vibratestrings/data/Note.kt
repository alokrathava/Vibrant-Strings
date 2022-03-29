package com.theworld.vibratestrings.data

data class Note(
    val note_id: String?,
    val title: String?,
    val url: String?,
) {
    // empty constructor to allow deserialization
    constructor() : this(null, null, null)
}
