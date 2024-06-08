package com.example.playlistmaker.mediateka.ui.models

sealed interface NewPlaylistState {

    data object Success : NewPlaylistState
    data object Error : NewPlaylistState
}
