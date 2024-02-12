package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.TrackPlayer
import com.example.playlistmaker.domain.models.PlayerState

class TrackPlayerInteractorImpl(private val player: TrackPlayer): MediaPlayerInteractor {

    override fun preparePlayer(runnable: Runnable) {
        player.preparePlayer(runnable)
    }

    override fun playbackControl(start: Runnable, stop: Runnable) {
        player.playbackControl(start, stop)
    }

    override fun startPlayer() {
        player.startPlayer()
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun releasePlayer() {
        player.releasePlayer()
    }

    override fun getState(): PlayerState {
       return player.getState()
    }

    override fun getCurrentPosition(): String {
        return player.getCurrentPosition()
    }
}