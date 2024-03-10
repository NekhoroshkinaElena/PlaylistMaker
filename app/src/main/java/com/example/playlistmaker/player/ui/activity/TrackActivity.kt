package com.example.playlistmaker.player.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.models.TrackScreenState
import com.example.playlistmaker.player.ui.view_model.TrackViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TRACK_KEY
import com.example.playlistmaker.util.dpToPx
import com.example.playlistmaker.util.getYearFromString
import com.example.playlistmaker.util.millisecondToMinute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val IMAGE_CORNER_RADIUS = 8f

class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private var track: Track? = null


    private val viewModel: TrackViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAudioPlayerScreen.setNavigationOnClickListener() {
            finish()
        }

        track = intent.getParcelableExtra(TRACK_KEY)

        val trackCoverUrl = track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        val trackTime = track?.trackTimeMillis ?: ""
        val album = track?.collectionName ?: ""

        Glide.with(applicationContext)
            .load(trackCoverUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(IMAGE_CORNER_RADIUS, this)))
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
