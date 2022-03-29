package com.theworld.vibratestrings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.theworld.vibratestrings.R
import android.content.Intent
import android.net.Uri
import android.view.View
import coil.load
import com.theworld.vibratestrings.databinding.ActivityChatBinding
import com.theworld.vibratestrings.databinding.ActivitySessionBinding

class SessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySessionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView13.load(R.drawable.online)

    }

    fun click(view: View?) {
        val url = "https://us05web.zoom.us/j/7613372199?pwd=YTZLQk95dS9KeWVHYzFiZ2RGcTE2UT09"
        val link = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(link)
    }
}