package com.example.playlistmaker.player.ui.models

sealed class TrackScreenState(
    val isPlayButtonEnabled: Boolean,
    val isPlayButtonActive: Boolean,
    val currentPosition: String
) {
    class Default : TrackScreenState(false, true, "00:00")
    class Prepared(currentPosition: String) : TrackScreenState(true, true, currentPosition)
    class Playing(currentPosition: String) : TrackScreenState(true, false, currentPosition)
    class Paused(currentPosition: String) : TrackScreenState(true, true, currentPosition)
}
