package com.example.playlistmaker.player.ui.models

//состояние экрана имеет два вида, состояние загрузки и показа контента
sealed class TrackScreenState {
    object Loading: TrackScreenState()
    data class Content(
        val trackModel: TrackModel,
    ): TrackScreenState()
}
