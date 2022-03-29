package com.theworld.vibratestrings.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.theworld.vibratestrings.data.Teacher
import com.theworld.vibratestrings.databinding.ActivityTeacherProfileBinding
import com.theworld.vibratestrings.ui.adapters.TeacherAdapter
import com.theworld.vibratestrings.ui.auth.LoginActivity
import com.theworld.vibratestrings.utils.startNewActivity

class TeacherProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TeacherProfileActivity"
    }

    private val context = this
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityTeacherProfileBinding

    private val teacherAdapter = TeacherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeacherProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        clickListener()
        fetchTeachers()
    }

    /*--------------------------------- On start ---------------------------------------*/
    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            startNewActivity(LoginActivity::class.java)
            return
        }

//        fetchUserData(currentUser)

    }


    private fun init() {

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.includedToolbar.toolbarTitle.text = "Teacher Profile"

        binding.apply {
            recyclerView.adapter = teacherAdapter
            recyclerView.setHasFixedSize(true)
        }

    }


    private fun clickListener() {

    }


    private fun fetchTeachers() {
        db.collection("teachers")
            .get()
            .addOnSuccessListener { document ->

                val teachers: List<Teacher> = document.toObjects(Teacher::class.java)
                Log.e(TAG, "fetchTeachers: $teachers")

                teacherAdapter.submitList(teachers)
            }

    }


}