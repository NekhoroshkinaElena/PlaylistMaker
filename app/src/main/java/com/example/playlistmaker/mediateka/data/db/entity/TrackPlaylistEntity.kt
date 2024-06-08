package com.example.playlistmaker.mediateka.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_in_playlist")
data class TrackPlaylistEntity(
    @PrimaryKey
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
