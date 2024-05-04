package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.ui.models.ThemeState
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class TracksApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TracksApplication)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        val settingsInteractor: SettingsInteractor by inject()

        when (settingsInteractor.getThemeSettings()) {
            is ThemeState.SystemTheme -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }

                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.MODE_NIGHT_YES
                    }
                }
            }

            is ThemeState.LightTheme -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            is ThemeState.DarkTheme -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
}
