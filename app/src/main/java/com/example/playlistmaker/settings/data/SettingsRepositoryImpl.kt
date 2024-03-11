package com.example.playlistmaker.settings.data

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.ui.models.ThemeState

private const val THEME_SWITCH_KEY = "theme_switch_key"

class SettingsRepositoryImpl(
    private val settingsPrefs: SharedPreferences,
    private val application: Application
) : SettingsRepository {

    override fun getThemeSettings(): ThemeState {
        if (!settingsPrefs.contains(THEME_SWITCH_KEY)
        ) {
            if (AppCompatDelegate.getDefaultNightMode()
                == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
            ) {
                return ThemeState.SystemTheme(
                    (application.resources.configuration.uiMode and
                            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                )
            }
        }

        return when (settingsPrefs.getBoolean(THEME_SWITCH_KEY, false)) {
            true -> ThemeState.DarkTheme
            false -> ThemeState.LightTheme
        }
    }

    override fun updateThemeSetting(isChecked: Boolean) {
        settingsPrefs.edit().putBoolean(THEME_SWITCH_KEY, isChecked).apply()
    }
}
