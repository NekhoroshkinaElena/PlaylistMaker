package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.util.millisecondToMinute

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAudioPlayerScreen.setNavigationOnClickListener() {
            finish()
        }

        val arguments = intent.extras

        val trackCoverUrl =
            arguments?.getString("trackCover")
                ?.replaceAfterLast('/', "512x512bb.jpg")

        val trackTime = arguments?.getString("trackTime") ?: ""
        val album = arguments?.getString("album") ?: ""

        Glide.with(applicationContext)
            .load(trackCoverUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.trackCover)

        binding.trackName.text = arguments?.getString("trackName")
        binding.artistName.text = arguments?.getString("artistName")
        binding.trackTime.text = millisecondToMinute(trackTime)
        binding.trackDurationValue.text = millisecondToMinute(trackTime)
        binding.trackName.text = arguments?.getString("trackName")
        binding.releaseDateValue.text = arguments?.getString("releaseDate")
        binding.countryValue.text = arguments?.getString("country")
        binding.genreValue.text = arguments?.getString("genre")

        if (!album.isNullOrBlank()) {
            binding.collectionNameValue.text = arguments?.getString("album")
            binding.collectionName.visibility = View.VISIBLE
            binding.collectionNameValue.visibility = View.VISIBLE
        }
    }
}
