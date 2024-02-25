package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.model.PlayerState

interface TrackPlayer {
    fun preparePlayer(runnable: Runnable)

    fun playbackControl(start: Runnable, stop: Runnable)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getState(): PlayerState

    fun getCurrentPosition(): String
}

