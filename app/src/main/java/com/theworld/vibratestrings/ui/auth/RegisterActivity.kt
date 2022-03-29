package com.theworld.vibratestrings.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.theworld.vibratestrings.data.User
import com.theworld.vibratestrings.databinding.ActivityRegisterBinding
import com.theworld.vibratestrings.ui.CourseActivity
import com.theworld.vibratestrings.utils.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"
    }

    private val user: FirebaseUser? = null
    private lateinit var binding: ActivityRegisterBinding
    private val context = this


    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        clickListener()
    }


    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            moveToDashboard()
        }
    }


    private fun init() {

        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

    }


    private fun clickListener() {

        binding.btnRegister.setOnClickListener {
            val name = binding.inputUsername.normalText()
            val email = binding.inputEmail.normalText()
            val password = binding.inputPassword.normalText()
            val confirmPassword = binding.inputConformPassword.normalText()

            if (
                !binding.inputEmail.customValidation(
                    CustomValidation(isEmail = true)
                )
                or
                !binding.inputUsername.customValidation(
                    CustomValidation()
                )
                or
                !binding.inputPassword.customValidation(
                    CustomValidation()
                )
                or
                !binding.inputConformPassword.customValidation(
                    CustomValidation()
                )
            ) {
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                toast("Please confirm password!")
            }

            doRegister(name, email, password);
        }

        binding.alreadyHaveAccount.setOnClickListener {
            finish()
        }
    }


    private fun doRegister(name: String, email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val uid = mAuth.uid
                    addInformationToDatabase(name, email, uid!!)
                } else {
                    Log.d(TAG, "ERROR:" + task.exception!!.message)
                    toast("ERROR:" + task.exception!!.message)
                }
            }.addOnFailureListener(context) { e: Exception ->
                Log.d(TAG, "ERROR:" + e.message)
                toast("ERROR:" + e.message)
            }

    }

    /*-----------------------------------------Add Information to firebase-------------------------*/
    private fun addInformationToDatabase(dName: String, dEmail: String, uid: String) {
        val dbRef = db.collection("users").document(uid)
        val referralNo: String = UUID.randomUUID().toString()

        val newUser = User(
            uid, dName, dEmail, referralNo
        )

        dbRef.set(newUser)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "getNewUserSnapShot: success")
                    toast("Register Successful")
                    moveToDashboard()
                } else {
                    Log.d(TAG, "getNewUserSnapShot: failed")
                    toast("Error:" + task.exception!!.message)
                }
            }
    }


    private fun moveToDashboard() {
        startNewActivity(CourseActivity::class.java)
    }


}