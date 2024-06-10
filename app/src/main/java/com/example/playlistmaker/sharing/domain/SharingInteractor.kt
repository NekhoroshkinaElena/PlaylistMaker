package com.example.playlistmaker.sharing.domain

interface SharingInteractor {

    fun shareApp()

    fun openTerms()

    fun openSupport()

    fun sharePlaylist(message: String)
}
