package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.model.PlayerState

interface MediaPlayerInteractor {
    fun preparePlayer(runnable: Runnable)

    fun playbackControl(start: Runnable, stop: Runnable)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getState(): PlayerState

    fun getCurrentPosition(): String

    fun loadTrackData(id: Int?, loadComplete: LoadComplete)

    interface LoadComplete{
        fun complete(onComplete: Any)
    }
}
