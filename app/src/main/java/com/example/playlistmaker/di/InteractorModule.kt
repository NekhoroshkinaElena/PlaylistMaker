package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.domain.FavoritesTracksInteractor
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.domain.impl.FavoritesTracksInteractorImpl
import com.example.playlistmaker.mediateka.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.impl.TrackPlayerInteractorImpl
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<TrackInteractor> {
        TracksInteractorImpl(trackRepository = get(), trackStorage = get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }

    factory<MediaPlayerInteractor> {
        TrackPlayerInteractorImpl(player = get())
    }

    factory<FavoritesTracksInteractor> {
        FavoritesTracksInteractorImpl(repository = get())
    }

    factory <PlaylistsInteractor>{
        PlaylistInteractorImpl(repository = get(), imageStorage = get())
    }
}
