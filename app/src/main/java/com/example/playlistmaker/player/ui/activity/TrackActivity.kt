package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.ui.models.TrackScreenState
import com.example.playlistmaker.player.ui.view_model.TrackViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TRACK_KEY
import com.example.playlistmaker.util.getYearFromString
import com.example.playlistmaker.util.millisecondToMinute

const val IMAGE_CORNER_RADIUS = 8

class TrackActivity : AppCompatActivity() {

    companion object {
        private const val TIME_UPDATE_DELAY = 500L
    }

    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var trackPlayer: MediaPlayerInteractor


    private lateinit var viewModel: TrackViewModel


    private val handler = Handler(Looper.getMainLooper())

    private val run = timerUpdater()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbarAudioPlayerScreen.setNavigationOnClickListener() {
            finish()
        }

        val track: Track? = intent.getParcelableExtra(TRACK_KEY)

//        viewModel = ViewModelProvider(this,
//            TrackViewModel.getViewModelFactory(track))[TrackViewModel::class.java]

        trackPlayer = Creator.provideTrackPlayer(track)

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

        trackPlayer.preparePlayer {
            handler.removeCallbacks(run)
            binding.playTrack.setImageResource(R.drawable.ic_play)
            binding.trackTime.text =
                millisecondToMinute(trackPlayer.getCurrentPosition())
        }

        binding.playTrack.setOnClickListener {
            trackPlayer.playbackControl(
                start = {
                    binding.playTrack.setImageResource(R.drawable.ic_button_pause)
                    handler.post(run)
                },
                stop = {
                    handler.removeCallbacks(run)
                    binding.playTrack.setImageResource(R.drawable.ic_play)
                })
        }
    }

    override fun onPause() {
        super.onPause()
        trackPlayer.pausePlayer()
        handler.removeCallbacks(run)
        binding.playTrack.setImageResource(R.drawable.ic_play)
    }

    override fun onDestroy() {
        handler.removeCallbacks(run)
        super.onDestroy()
        trackPlayer.releasePlayer()
    }

    private fun timerUpdater(): Runnable {
        return object : Runnable {
            override fun run() {
                if (trackPlayer.getState() == PlayerState.PLAYING) {
                    binding.trackTime.text =
                        millisecondToMinute(trackPlayer.getCurrentPosition())
                    handler.postDelayed(this, TIME_UPDATE_DELAY)
                }
            }
        }
    }
}
