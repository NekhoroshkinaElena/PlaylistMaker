package com.example.playlistmaker.track

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val collectionName: String,
    val country: String
)
