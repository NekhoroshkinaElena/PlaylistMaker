package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.ui.models.ThemeState

interface SettingsInteractor {
    fun getThemeSettings(): ThemeState
    fun updateThemeSetting(isChecked: Boolean)
}
