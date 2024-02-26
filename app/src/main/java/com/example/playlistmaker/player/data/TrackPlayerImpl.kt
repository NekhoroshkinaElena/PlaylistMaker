package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.model.Track

class TrackPlayerImpl(private val track: Track?) : TrackPlayer {

    private var playerState = PlayerState.DEFAULT

    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PAUSED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            mediaPlayer.seekTo(0)
        }
    }

    override fun playbackControl() {
        if (playerState == PlayerState.PLAYING) {
            pausePlayer()
        } else if (playerState == PlayerState.PREPARED || playerState == PlayerState.PAUSED) {
            startPlayer()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pausePlayer() {
        playerState = PlayerState.PAUSED
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getState(): PlayerState {
        return playerState
    }

    override fun getCurrentPosition(): String {
        return mediaPlayer.currentPosition.toString()
    }
}
