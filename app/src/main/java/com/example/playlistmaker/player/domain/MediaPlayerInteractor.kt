package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.models.PlayerState

interface MediaPlayerInteractor {
    fun preparePlayer()

    fun playbackControl()

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getState(): PlayerState

    fun getCurrentPosition(): String
}
