package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TracksListResponse

interface TrackRepository {
    fun searchTracks(text: String): TracksListResponse
}
