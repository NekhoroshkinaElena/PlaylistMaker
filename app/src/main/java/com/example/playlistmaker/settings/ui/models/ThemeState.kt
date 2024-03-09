package com.example.playlistmaker.settings.ui.models

sealed interface ThemeState {
    data class SystemTheme(var isChecked: Boolean) : ThemeState

    data object LightTheme : ThemeState

    data object DarkTheme : ThemeState
}
