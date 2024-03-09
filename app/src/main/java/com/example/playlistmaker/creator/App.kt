package com.example.playlistmaker.creator

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.ui.models.ThemeState

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val settingsInteractor = Creator.provideSettingsInteractor(this)

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
