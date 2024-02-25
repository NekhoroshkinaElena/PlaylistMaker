package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitRequester
import com.example.playlistmaker.player.data.TrackPlayerImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TrackStorage
import com.example.playlistmaker.player.domain.impl.TrackPlayerInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.model.Track

object Creator {
    private fun getTracksRepository(context: Context): TrackRepository {
        return TracksRepositoryImpl(RetrofitRequester(context))
    }

    fun provideTracksInteractor(trackStorage: TrackStorage, context: Context): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository(context), trackStorage)
    }

    private fun getTrackPlayer(track: Track?): TrackPlayer {
        return TrackPlayerImpl(track)
    }

    fun provideTrackPlayer(track: Track?): MediaPlayerInteractor {
        return TrackPlayerInteractorImpl(getTrackPlayer(track))
    }
}
