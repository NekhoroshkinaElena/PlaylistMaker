package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface TrackPlayer {
    fun preparePlayer(runnable: Runnable)

    fun playbackControl(start: Runnable, stop: Runnable)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getState(): PlayerState

    fun getCurrentPosition(): String
}

