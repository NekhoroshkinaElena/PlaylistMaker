package com.example.playlistmaker.mediateka.ui.models

import com.example.playlistmaker.mediateka.domain.model.Playlist

sealed interface PlaylistsState {
    data class Content(
        val playList: List<Playlist>
    ) : PlaylistsState

    data class Empty(val playlist: List<Playlist>) : PlaylistsState
}
