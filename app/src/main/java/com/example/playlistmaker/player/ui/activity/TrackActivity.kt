package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.models.TrackScreenState
import com.example.playlistmaker.player.ui.view_model.TrackViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TRACK_KEY
import com.example.playlistmaker.util.getYearFromString
import com.example.playlistmaker.util.millisecondToMinute

const val IMAGE_CORNER_RADIUS = 8

class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var viewModel: TrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbarAudioPlayerScreen.setNavigationOnClickListener() {
            finish()
        }

        val track: Track? = intent.getParcelableExtra(TRACK_KEY)

        viewModel = ViewModelProvider(
            this, TrackViewModel.getViewModelFactory(track)
        )[TrackViewModel::class.java]

        val trackCoverUrl = track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        val trackTime = track?.trackTimeMillis ?: ""
        val album = track?.collectionName ?: ""

        Glide.with(applicationContext)
            .load(trackCoverUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(IMAGE_CORNER_RADIUS))
            .into(binding.trackCover)

        binding.trackName.text = track?.trackName
        binding.artistName.text = track?.artistName
        binding.trackTime.text = millisecondToMinute(trackTime)
        binding.trackDurationValue.text = millisecondToMinute(trackTime)
        binding.releaseDateValue.text = getYearFromString(track?.releaseDate)
        binding.countryValue.text = track?.country
        binding.genreValue.text = track?.primaryGenreName

        if (album.isNotBlank()) {
            binding.collectionNameValue.text = album
            binding.collectionName.isVisible = true
            binding.collectionNameValue.isVisible = true
        }

        viewModel.preparePlayer()

        binding.playTrack.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getScreenStateMediaPlayer().observe(this) {
            render(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun render(trackScreenState: TrackScreenState) {
        when (trackScreenState) {
            is TrackScreenState.Loading -> {
                preparePlayer()
            }

            is TrackScreenState.Prepared -> {
                showPrepared(trackScreenState.trackDuration)
            }

            is TrackScreenState.Play -> {
                showPlayback(trackScreenState.currentPosition)
            }

            is TrackScreenState.Pause -> {
                showPause()
            }
        }
    }

    private fun preparePlayer() {
        binding.playTrack.isClickable = false
    }

    private fun showPrepared(currentPosition: String) {
        binding.playTrack.isClickable = true
        binding.playTrack.setImageResource(R.drawable.ic_play)
        binding.trackTime.text = currentPosition
    }

    private fun showPlayback(currentPosition: String) {
        binding.playTrack.setImageResource(R.drawable.ic_button_pause)
        binding.trackTime.text = currentPosition
    }

    private fun showPause() {
        binding.playTrack.setImageResource(R.drawable.ic_play)
    }
}
