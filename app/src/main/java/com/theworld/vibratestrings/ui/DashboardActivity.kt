package com.theworld.vibratestrings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.theworld.vibratestrings.databinding.ActivityDashboardBinding
import com.theworld.vibratestrings.ui.auth.LoginActivity
import com.theworld.vibratestrings.utils.openActivity
import com.theworld.vibratestrings.utils.startNewActivity

class DashboardActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val context = this
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        clickListener()
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
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.includedToolbar.toolbarTitle.text = "Dashboard"

    }


    private fun clickListener() {

        binding.userProfile.setOnClickListener {
            openActivity(UserProfileActivity::class.java)
        }

        binding.teacherProfile.setOnClickListener {
            openActivity(TeacherListActivity::class.java)
        }

        binding.chatBox.setOnClickListener {
            openActivity(ChatActivity::class.java)
        }

        binding.notes.setOnClickListener {
            openActivity(NoteActivity::class.java)
        }

        binding.session.setOnClickListener {
            openActivity(SessionActivity::class.java)
        }

        binding.logout.setOnClickListener {
            mAuth.signOut()
            startNewActivity(LoginActivity::class.java)
        }

    }


}