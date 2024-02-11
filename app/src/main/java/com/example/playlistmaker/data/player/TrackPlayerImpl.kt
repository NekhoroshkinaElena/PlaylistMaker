package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.TrackPlayer
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track

class TrackPlayerImpl(private val track: Track?) : TrackPlayer {

    private var playerState = PlayerState.DEFAULT

    private val mediaPlayer = MediaPlayer()

    override fun preparePlayer(runnable: Runnable) {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PAUSED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            mediaPlayer.seekTo(0)
            runnable.run()
        }
    }

    override fun playbackControl(start: Runnable, stop: Runnable) {
        if (playerState == PlayerState.PLAYING) {
            pausePlayer()
            stop.run()
        } else if (playerState == PlayerState.PREPARED || playerState == PlayerState.PAUSED) {
            startPlayer()
            start.run()
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
