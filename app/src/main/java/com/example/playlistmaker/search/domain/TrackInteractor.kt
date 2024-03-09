package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult

interface TrackInteractor {
    fun searchTrack(text: String, consumer: TracksConsumer)

    fun getSearchHistory(): ArrayList<Track>

    fun addTrackToHistory(track: Track)

    fun clearTheHistory()

    interface TracksConsumer {
        fun consume(tracksListResult: TracksListResult)
    }
}
