package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.data.external_storage.ImageStorageImpl
import com.example.playlistmaker.mediateka.data.impl.FavoritesTrackRepositoryImpl
import com.example.playlistmaker.mediateka.data.impl.PlaylistsRepositoryImpl
import com.example.playlistmaker.mediateka.domain.FavoritesTracksRepository
import com.example.playlistmaker.mediateka.domain.ImageStorage
import com.example.playlistmaker.mediateka.domain.PlaylistsRepository
import com.example.playlistmaker.search.data.history.TrackHistoryStorage
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TrackStorage
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TracksRepositoryImpl(get())
    }

    single<TrackStorage> {
        TrackHistoryStorage(
            historyPrefs = get(named(SEARCH_HISTORY_PREFERENCES)), get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            settingsPrefs = get(named(PLAYLIST_MAKER_PREFERENCES)),
            application = get()
        )
    }

    single<FavoritesTracksRepository> {
        FavoritesTrackRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(playlistDb = get(), converter = get())
    }

    single<ImageStorage> {
        ImageStorageImpl(context = androidContext())
    }
}
