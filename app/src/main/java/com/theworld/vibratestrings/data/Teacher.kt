package com.theworld.vibratestrings.data

import com.google.firebase.firestore.DocumentId

data class Teacher(
    val teacher_id: String?,
    val name: String?,
) {
    // empty constructor to allow deserialization
    constructor() : this(null, null)
}
