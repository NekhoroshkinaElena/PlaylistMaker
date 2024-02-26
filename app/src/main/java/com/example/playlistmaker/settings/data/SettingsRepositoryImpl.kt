package com.example.playlistmaker.settings.data

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.App
import com.example.playlistmaker.creator.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.creator.THEME_SWITCH_KEY
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val application: Application) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        if (!application.getSharedPreferences(
                PLAYLIST_MAKER_PREFERENCES,
                Application.MODE_PRIVATE
            ).contains(THEME_SWITCH_KEY)
        ) {
            if (AppCompatDelegate.getDefaultNightMode()
                == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
            ) {
                return ThemeSettings.SystemTheme(
                    (application.resources.configuration.uiMode and
                            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                )
            }
        }
        return if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            ThemeSettings.DarkTheme
        } else {
            ThemeSettings.LightTheme
        }
    }

    override fun updateThemeSetting(isChecked: Boolean) {
        (application.applicationContext as App).switchTheme(isChecked)
    }
}