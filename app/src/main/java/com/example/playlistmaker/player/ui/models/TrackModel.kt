package com.example.playlistmaker.player.ui.models

//класс модель для передачи данных между слоями view and viewModel
data class TrackModel(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val collectionName: String,
    val country: String,
    val previewUrl: String
)
