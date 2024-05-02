package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(text: String): Flow<TracksListResult?>

    fun getSearchHistory(): ArrayList<Track>

    fun addTrackToHistory(track: Track)

    fun clearTheHistory()
}
