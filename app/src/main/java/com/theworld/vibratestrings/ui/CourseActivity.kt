package com.theworld.vibratestrings.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.theworld.vibratestrings.R
import com.theworld.vibratestrings.databinding.ActivitySelectCourseBinding
import com.theworld.vibratestrings.ui.auth.LoginActivity
import com.theworld.vibratestrings.utils.confirmDialog
import com.theworld.vibratestrings.utils.startNewActivity
import com.theworld.vibratestrings.utils.toast
import java.util.*

class CourseActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CourseActivity"
    }

    private val context = this
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var binding: ActivitySelectCourseBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectCourseBinding.inflate(layoutInflater)
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

        binding.includedToolbar.toolbarTitle.text = "Select Course"

    }


    private fun clickListener() {

        binding.guitar.setOnClickListener {
            displayConfirmationDialog("Guitar")
        }

        binding.classical.setOnClickListener {
            displayConfirmationDialog("Classical")
        }

        binding.harmonium.setOnClickListener {
            displayConfirmationDialog("Harmonium")
        }

        binding.keyboard.setOnClickListener {
            displayConfirmationDialog("Keyboard")
        }

        binding.production.setOnClickListener {
            displayConfirmationDialog("Production")
        }

        binding.recording.setOnClickListener {
            displayConfirmationDialog("Recording")
        }

    }

    private fun displayConfirmationDialog(name: String) {

        val message = getString(R.string.confirmation_dialog_title, name)
        confirmDialog(message = message) {

            val updateData = mutableMapOf<String, Any>()
            updateData["interested_in"] = name
            updateData["created_at"] = System.currentTimeMillis()

            db.collection("users")
                .document(mAuth.currentUser!!.uid)
//                .update("interested_in", name)
                .update(updateData)
                .addOnSuccessListener {
                    Log.d(TAG, "Update Success")
                    startNewActivity(DashboardActivity::class.java)
                }
                .addOnFailureListener {
                    Log.d(TAG, "Course Selection: Failed :::: ${it.message}")
                    toast("Course selection failed")
                }

        }

    }


}