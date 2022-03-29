package com.theworld.vibratestrings.ui

import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.theworld.vibratestrings.R
import com.theworld.vibratestrings.databinding.ActivityPlayerBinding
import com.theworld.vibratestrings.utils.toast

class PlayerActivity : YouTubeBaseActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        init()
    }

    private fun init() {

        val videoLink = "e4d0LOuP4Uw"

        binding.ytPlayer.initialize(
            getString(R.string.youtube_api_key),
            object : YouTubePlayer.OnInitializedListener {
                // Implement two methods by clicking on red error bulb
                // inside onInitializationSuccess method
                // add the video link or the
                // playlist link that you want to play
                // In here we also handle the play and pause functionality
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider?,
                    player: YouTubePlayer?,
                    p2: Boolean
                ) {
                    player?.loadVideo(videoLink)
                    player?.play()
                }

                // Inside onInitializationFailure
                // implement the failure functionality
                // Here we will show toast
                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    toast("Video player Failed")
                }
            })
    }

}