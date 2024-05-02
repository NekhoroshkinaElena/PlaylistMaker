package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.model.Track

class TrackPlayerImpl(
    private val track: Track?,
    private val mediaPlayer: MediaPlayer
) : TrackPlayer {

    private var playerState = PlayerState.DEFAULT

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
        when (playerState) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PAUSED, PlayerState.PREPARED -> startPlayer()
            PlayerState.CHANGED_CONFIG -> {
                startPlayer()
            }

            else -> {}
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pausePlayer() {
        playerState = if (getState() == PlayerState.CHANGED_CONFIG) {
            PlayerState.CHANGED_CONFIG
        } else {
            PlayerState.PAUSED
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onChangedConfig() {
        playerState = PlayerState.CHANGED_CONFIG
    }

    override fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState = PlayerState.DEFAULT
    }

    override fun getState(): PlayerState {
        return playerState
    }

    override fun getCurrentPosition(): String {
        return mediaPlayer.currentPosition.toString()
    }
}
