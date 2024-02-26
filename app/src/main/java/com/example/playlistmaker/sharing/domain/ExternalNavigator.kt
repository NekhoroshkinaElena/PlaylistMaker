package com.example.playlistmaker.sharing.domain

interface ExternalNavigator {

    fun shareLink()

    fun openLink()

    fun openEmail()
}
