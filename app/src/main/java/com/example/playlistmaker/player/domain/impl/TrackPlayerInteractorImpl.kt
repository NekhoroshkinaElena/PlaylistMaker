package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.player.domain.models.PlayerState

class TrackPlayerInteractorImpl(private val player: TrackPlayer) : MediaPlayerInteractor {

    override fun preparePlayer() {
        player.preparePlayer()
    }

    override fun playbackControl() {
        player.playbackControl()
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

    override fun onChangeConfig() {
        player.onChangedConfig()
    }

    override fun getState(): PlayerState {
        return player.getState()
    }

    override fun getCurrentPosition(): String {
        return player.getCurrentPosition()
    }
}
