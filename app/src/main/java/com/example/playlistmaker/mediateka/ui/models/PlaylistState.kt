package com.example.playlistmaker.mediateka.ui.models

import com.example.playlistmaker.search.domain.model.Track

sealed interface PlaylistState {
    data class Content(
        val playList: List<List<Track>>
    ) : PlaylistState

    data object Empty : PlaylistState
}
