package com.theworld.vibratestrings.data

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val referral: String,
    val created_at: String = "",
    val interested_in: String = "",
)
