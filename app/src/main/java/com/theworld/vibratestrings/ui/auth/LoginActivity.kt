package com.theworld.vibratestrings.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.protobuf.Empty
import com.theworld.vibratestrings.data.User
import com.theworld.vibratestrings.databinding.ActivityLoginBinding
import com.theworld.vibratestrings.ui.CourseActivity
import com.theworld.vibratestrings.ui.DashboardActivity
import com.theworld.vibratestrings.utils.*
import java.util.*


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private var user: FirebaseUser? = null
    private lateinit var binding: ActivityLoginBinding
    private val context = this


    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()
        clickListener()
    }


    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            fetchUserData(currentUser)
        }
    }


    private fun init() {

        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

    }


    private fun clickListener() {

        binding.btnlogin.setOnClickListener {
            val email = binding.inputEmail.normalText()
            val password = binding.inputPassword.normalText()

            if (
                !binding.inputEmail.customValidation(
                    CustomValidation(isEmail = true)
                )
                or
                !binding.inputPassword.customValidation(
                    CustomValidation()
                )
            ) {
                return@setOnClickListener
            }

            doLogin(email, password)
        }

        binding.textViewSignUp.setOnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
        }
    }


    private fun doLogin(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    toast("Login Successful")
                    user = mAuth.currentUser
                    updateUI(user)
                } else {
                    toast("ERROR:" + task.exception!!.message)
                }
            }.addOnFailureListener(context) { e: Exception ->
                toast("ERROR:" + e.message)
            }

    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let {
            val name = user.displayName
            val email = user.email
            val uid = user.uid


            db.collection("users").document(uid)
                .get()
                .addOnCompleteListener { task: Task<DocumentSnapshot> ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            Log.d(TAG, "Document exists!")
                            fetchUserData(mAuth.currentUser!!)
                        } else {
                            Log.d(TAG, "Document does not exist!")
                            addInformationToDatabase(name!!, email!!, uid)
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.exception)
                    }
                }
        }
    }


    /*-----------------------------------------Add Information to firebase-------------------------*/
    private fun addInformationToDatabase(dName: String, dEmail: String, uid: String) {
        val dbRef = db.collection("users").document(uid)
        val referralNo: String = UUID.randomUUID().toString()
        val loginModel = User(
            uid, dName, dEmail, referralNo
        )

        dbRef.set(loginModel)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "getNewUserSnapShot: success")
                    fetchUserData(mAuth.currentUser!!)
                } else {
                    Log.d(TAG, "getNewUserSnapShot: failed")
                    toast("Error:" + task.exception!!.message)
                }
            }
    }


    private fun moveToDashboard() {
        startNewActivity(CourseActivity::class.java)
    }


    /*-------------------------------- Fetch user Data --------------------------------------------*/

    private fun fetchUserData(currentUser: FirebaseUser) {

        val id = currentUser.uid

        db.collection("users")
            .document(id)
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    val document = it.result

                    val interestedIn = document.getString("interested_in")

                    if (interestedIn.isNullOrEmpty()) {
                        startNewActivity(CourseActivity::class.java)
                    } else {
                        startNewActivity(DashboardActivity::class.java)
                    }

                }
            }


    }


}