package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TracksListResult

interface TrackRepository {
    fun searchTracks(text: String): TracksListResult
}
