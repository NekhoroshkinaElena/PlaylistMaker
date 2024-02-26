package com.example.playlistmaker.settings.domain.model

sealed interface ThemeSettings {
    data class SystemTheme(var isChecked: Boolean) : ThemeSettings

    data object LightTheme : ThemeSettings

    data object DarkTheme : ThemeSettings
}
