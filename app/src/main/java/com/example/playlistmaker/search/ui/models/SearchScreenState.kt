package com.example.playlistmaker.search.ui.models

import com.example.playlistmaker.search.domain.model.Track

sealed interface SearchScreenState {
    data object Loading : SearchScreenState

    data class Content(
        val tracks: List<Track>
    ) : SearchScreenState

    data object Error : SearchScreenState

    data object Empty : SearchScreenState

    data object Unfocused : SearchScreenState

    data class History(
        val tracks: List<Track>
    ) : SearchScreenState
}
