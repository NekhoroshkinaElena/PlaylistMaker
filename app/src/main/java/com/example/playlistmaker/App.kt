package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val THEME_SWITCH_KEY = "theme_switch_key"

class App : Application() {

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        if (!hasRecordInSharedPreferences()) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    AppCompatDelegate.MODE_NIGHT_NO
                }

                Configuration.UI_MODE_NIGHT_YES -> {
                    AppCompatDelegate.MODE_NIGHT_YES
                }
            }
        }
        if (hasRecordInSharedPreferences()) {
            darkTheme = sharedPrefs.getBoolean(THEME_SWITCH_KEY, false)
            switchTheme(darkTheme)
        }
    }

    fun hasRecordInSharedPreferences(): Boolean {
        if (sharedPrefs.contains(THEME_SWITCH_KEY)) {
            return true
        }
        return false
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPrefs.edit().putBoolean(THEME_SWITCH_KEY, darkTheme).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
