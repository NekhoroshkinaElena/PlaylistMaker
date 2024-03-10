package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TracksRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(settingsPrefs = get(named("settingsPrefs")), application = get())
    }
}
