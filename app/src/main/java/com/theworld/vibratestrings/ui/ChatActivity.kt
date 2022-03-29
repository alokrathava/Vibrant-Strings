package com.theworld.vibratestrings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.theworld.vibratestrings.R
import android.content.Intent
import android.net.Uri
import android.view.View
import coil.load
import com.theworld.vibratestrings.databinding.ActivityChatBinding
import com.theworld.vibratestrings.databinding.ActivityDashboardBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView14.load(R.drawable.chatroom)

    }

    fun click(view: View?) {
        val url = "https://chat.whatsapp.com/GsLd9XUEdHFEa5E577LevA"
        val link = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(link)
    }
}