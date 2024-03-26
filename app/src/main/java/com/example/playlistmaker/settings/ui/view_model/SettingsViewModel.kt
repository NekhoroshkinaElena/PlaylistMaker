package com.example.playlistmaker.settings.ui.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.ui.models.ThemeState
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val stateThemeApp: MutableLiveData<ThemeState> =
        MutableLiveData()

    fun getStateThemeApp(): LiveData<ThemeState> = stateThemeApp

    fun renderCurrentTheme() {
        render(settingsInteractor.getThemeSettings())
    }

    private fun render(themeState: ThemeState) {
        stateThemeApp.postValue(themeState)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun updateTheme(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        settingsInteractor.updateThemeSetting(isChecked)
        render(settingsInteractor.getThemeSettings())
    }
}
