package com.theworld.vibratestrings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.theworld.vibratestrings.data.Note
import com.theworld.vibratestrings.data.Teacher
import com.theworld.vibratestrings.databinding.ActivityTeacherProfileBinding
import com.theworld.vibratestrings.ui.adapters.NoteAdapter
import com.theworld.vibratestrings.ui.auth.LoginActivity
import com.theworld.vibratestrings.utils.startNewActivity
import com.theworld.vibratestrings.utils.toast
import java.lang.Exception

class NoteActivity : AppCompatActivity(), NoteAdapter.NoteListener {

    companion object {
        private const val TAG = "NoteActivity"
    }

    private val context = this
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityTeacherProfileBinding

    private val noteAdapter = NoteAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeacherProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        clickListener()
        fetchNotes()
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

        binding.includedToolbar.toolbarTitle.text = "Notes"

        binding.apply {
            recyclerView.adapter = noteAdapter
            recyclerView.setHasFixedSize(true)
        }

    }


    private fun clickListener() {

    }


    private fun fetchNotes() {
        db.collection("notes")
            .get()
            .addOnSuccessListener { document ->

                val notes: List<Note> = document.toObjects(Note::class.java)
                Log.e(TAG, "fetchNotes: $notes")

                noteAdapter.submitList(notes)
            }

    }

    override fun onNoteClick(note: Note) {
        try {
            Intent().also {
                it.action = Intent.ACTION_VIEW
                it.setDataAndType(Uri.parse(note.url), "application/pdf")
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "onNoteClick: ${e.message}")
            toast(e.message)
        }
    }


}