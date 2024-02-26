package com.example.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val stateThemeApp: MutableLiveData<ThemeSettings> =
        MutableLiveData()

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SettingsViewModel(
                    Creator.provideSharingInteractor(application),
                    Creator.provideSettingsInteractor(application)
                )
            }
        }
    }

    fun getStateThemeApp(): MutableLiveData<ThemeSettings> = stateThemeApp

    private fun render(themeSettings: ThemeSettings) {
        stateThemeApp.postValue(themeSettings)
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

    fun getThemeSettings() {
        render(settingsInteractor.getThemeSettings())
    }

    fun updateTheme(isChecked: Boolean) {
        settingsInteractor.updateThemeSetting(isChecked)
        render(settingsInteractor.getThemeSettings())
    }
}
