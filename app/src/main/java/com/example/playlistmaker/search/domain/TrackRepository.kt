package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.TracksListResult

interface TrackRepository {
    fun searchTracks(text: String): TracksListResult
}
