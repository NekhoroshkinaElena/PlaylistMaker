package com.example.playlistmaker

import com.example.playlistmaker.player.data.TrackPlayerImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.player.domain.impl.TrackPlayerInteractorImpl
import com.example.playlistmaker.search.domain.model.Track


object Creator {
    private fun getTrackPlayer(track: Track?): TrackPlayer {
        return TrackPlayerImpl(track)
    }

    fun provideTrackPlayerInteractor(track: Track?): MediaPlayerInteractor {
        return TrackPlayerInteractorImpl(getTrackPlayer(track))
    }
}