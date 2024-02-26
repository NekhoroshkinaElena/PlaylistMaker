package com.example.playlistmaker.player.ui.models

sealed interface TrackScreenState {
    data class Prepared(var trackDuration: String) : TrackScreenState
    data class Play(var currentPosition: String) : TrackScreenState
    data class Pause(var currentPosition: String) : TrackScreenState
    data object Loading : TrackScreenState
}
