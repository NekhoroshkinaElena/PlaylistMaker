package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.ui.models.ThemeState

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeState {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(isChecked: Boolean) {
        return settingsRepository.updateThemeSetting(isChecked)
    }
}
