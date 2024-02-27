package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.ui.models.ThemeState

interface SettingsRepository {
    fun getThemeSettings(): ThemeState
    fun updateThemeSetting(isChecked: Boolean)
}
