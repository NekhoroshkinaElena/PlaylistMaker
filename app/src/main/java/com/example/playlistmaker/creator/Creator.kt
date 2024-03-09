package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.player.data.TrackPlayerImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.player.domain.impl.TrackPlayerInteractorImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitRequester
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TrackStorage
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

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

    fun provideTrackPlayerInteractor(track: Track?): MediaPlayerInteractor {
        return TrackPlayerInteractorImpl(getTrackPlayer(track))
    }

    private fun getSettingsRepository(application: Application): SettingsRepository {
        return SettingsRepositoryImpl(application)
    }

    fun provideSettingsInteractor(application: Application): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(application))
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }
}
