package com.example.playlistmaker.mediateka.ui.models

import com.example.playlistmaker.mediateka.domain.model.Playlist

sealed interface PlaylistState {
    data class Content(
        val playList: List<Playlist>
    ) : PlaylistState

    data object Empty : PlaylistState
}
