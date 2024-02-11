package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TrackStorage {

    fun getSearchHistory(): ArrayList<Track>

    fun saveSearchHistory(tracks: List<Track>)

    fun clearHistory()
}
