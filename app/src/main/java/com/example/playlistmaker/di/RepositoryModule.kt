package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.history.TrackHistoryStorage
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TrackStorage
import com.example.playlistmaker.search.ui.activity.SEARCH_HISTORY_PREFERENCES
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TracksRepositoryImpl(get())
    }

    single<TrackStorage> {
        TrackHistoryStorage(historyPrefs = get(named(SEARCH_HISTORY_PREFERENCES)), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(settingsPrefs = get(named(PLAYLIST_MAKER_PREFERENCES)), application = get())
    }
}
