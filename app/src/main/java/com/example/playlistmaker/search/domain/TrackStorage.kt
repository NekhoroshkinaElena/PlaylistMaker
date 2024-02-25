package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface TrackStorage {

    fun getSearchHistory(): ArrayList<Track>

    fun saveSearchHistory(tracks: List<Track>)

    fun clearHistory()
}
