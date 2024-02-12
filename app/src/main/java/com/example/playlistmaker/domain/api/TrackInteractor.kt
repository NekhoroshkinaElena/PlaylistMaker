package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksListResult

interface TrackInteractor {
    fun searchTrack(text: String, consumer: TracksConsumer)

    fun getSearchHistory(): ArrayList<Track>

    fun addTrackToHistory(track: Track)

    fun clearTheHistory()

    interface TracksConsumer {
        fun consume(tracksListResult: TracksListResult)
    }
}
