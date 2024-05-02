package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.TracksListResult
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(text: String): Flow<TracksListResult?>
}
