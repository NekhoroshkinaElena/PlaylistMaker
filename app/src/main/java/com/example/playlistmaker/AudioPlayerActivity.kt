package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.track.TRACK_KEY
import com.example.playlistmaker.track.Track
import com.example.playlistmaker.util.getYearFromString
import com.example.playlistmaker.util.millisecondToMinute

const val IMAGE_CORNER_RADIUS = 8

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_UPDATE_DELAY = 500L
    }

    private var playerState = STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()

    private lateinit var binding: ActivityAudioPlayerBinding

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

        preparePlayer(track?.previewUrl)

        binding.playTrack.setOnClickListener {
            playbackControl()
        }
    }

    private fun preparePlayer(previewUrl: String?) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            handler.removeCallbacks(run)
            playerState = STATE_PREPARED
            binding.playTrack.setImageResource(R.drawable.ic_play)
            mediaPlayer.seekTo(0)
            binding.trackTime.text =
                millisecondToMinute(mediaPlayer.currentPosition.toString())
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PAUSED, STATE_PREPARED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playTrack.setImageResource(R.drawable.ic_button_pause)
        playerState = STATE_PLAYING
        handler.post(run)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playTrack.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(run)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        handler.removeCallbacks(run)
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun timerUpdater(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    binding.trackTime.text =
                        millisecondToMinute(mediaPlayer.currentPosition.toString())
                    handler.postDelayed(this, TIME_UPDATE_DELAY)
                }
            }
        }
    }
}

