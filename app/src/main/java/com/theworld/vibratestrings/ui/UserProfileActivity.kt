package com.theworld.vibratestrings.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.theworld.vibratestrings.R
import com.theworld.vibratestrings.databinding.ActivityUserProfileBinding
import com.theworld.vibratestrings.ui.auth.LoginActivity
import com.theworld.vibratestrings.utils.*
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "UserProfileActivity"
    }

    private val context = this
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var binding: ActivityUserProfileBinding

    private var referralCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        clickListener()
        fetchUserProfile()
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

        binding.includedToolbar.toolbarTitle.text = "User Profile"

    }


    private fun clickListener() {

        binding.btnSubmit.setOnClickListener {

            if (!binding.edtFullName.customValidation(
                    CustomValidation()
                )
            ) {
                return@setOnClickListener
            }

            val name = binding.edtFullName.normalText()
            updateUserProfile(name)

        }


        binding.edtReferral.editText!!.setOnClickListener {
            val message = "My Referral Code is $referralCode \n \n - " + getString(R.string.app_name)

            Intent().also {
                it.type = "text/plain"
                it.action = Intent.ACTION_SEND
                it.putExtra(Intent.EXTRA_TEXT, message)
                it.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))

                startActivity(Intent.createChooser(it, "Send to: "))
            }


        }


    }

    private fun updateUserProfile(name: String) {
        db.collection("users")
            .document(mAuth.currentUser!!.uid)
            .update("name", name)
            .addOnSuccessListener {
                Log.d(TAG, "Update Success")
                toast("Profile Updated")
                startNewActivity(DashboardActivity::class.java)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed :::: ${it.message}")
                toast("Error ${it.message}")
            }
    }


    private fun fetchUserProfile() {
        db.collection("users")
            .document(mAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->

                binding.loadingSpinner.isVisible = false
                val userData = document.data

                binding.edtFullName.editText!!.setText(userData?.get("name").toString())
                binding.edtEmail.editText!!.setText(userData?.get("email").toString())
                binding.edtReferral.editText!!.setText(userData?.get("referral").toString())
                binding.edtCourse.editText!!.setText(userData?.get("interested_in").toString())

                referralCode = userData?.get("referral").toString()

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = userData?.get("created_at").toString().toLong()
                calendar.add(Calendar.MONTH, 1)
                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)

                binding.edtValidity.editText!!.setText(date)

            }

    }


}