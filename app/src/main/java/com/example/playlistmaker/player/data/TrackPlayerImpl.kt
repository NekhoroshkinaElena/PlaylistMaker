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
        if (playerState == PlayerState.PLAYING) {
            pausePlayer()
        } else if (playerState == PlayerState.CHANGED_CONFIG) {
            startPlayer()
        } else if (playerState == PlayerState.PREPARED || playerState == PlayerState.PAUSED) {
            startPlayer()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun pausePlayer() {
        if (getState() == PlayerState.PREPARED) {
            return
        } else if (playerState == PlayerState.CHANGED_CONFIG) {
            playerState = if(getCurrentPosition().toInt() == 0){
                PlayerState.CHANGED_CONFIG
            } else {
                PlayerState.PLAYING
            }
        } else {
            playerState = PlayerState.PAUSED
            mediaPlayer.pause()
        }

    }

    override fun onChangedConfig() {
        playerState = PlayerState.CHANGED_CONFIG
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
