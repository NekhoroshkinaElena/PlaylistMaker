package com.example.playlistmaker.search.ui.models

import com.example.playlistmaker.search.domain.model.Track

sealed interface SearchState {
    data object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data object Error : SearchState

    data object Empty : SearchState

    data object Unfocused : SearchState

    data class History(
        val tracks: List<Track>
    ) : SearchState
}
