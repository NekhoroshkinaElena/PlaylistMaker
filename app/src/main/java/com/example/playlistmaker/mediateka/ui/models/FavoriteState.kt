package com.example.playlistmaker.mediateka.ui.models

import com.example.playlistmaker.search.domain.model.Track

sealed interface FavoriteState {
    data class Content(
        val tracks: List<Track>
    ) : FavoriteState

    data object Empty : FavoriteState
}
