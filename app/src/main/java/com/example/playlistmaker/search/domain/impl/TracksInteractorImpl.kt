package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TrackStorage
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(
    private val trackRepository: TrackRepository,
    private val trackStorage: TrackStorage
) : TrackInteractor {

    override fun searchTrack(text: String): Flow<TracksListResult?> {
        return trackRepository.searchTracks(text)
    }

    override fun getSearchHistory(): ArrayList<Track> {
        return trackStorage.getSearchHistory()
    }

    override fun addTrackToHistory(track: Track) {
        val tracks: MutableList<Track> = getSearchHistory().toMutableList()
        if (tracks.size >= 10) {
            tracks.removeAt(0)
        }
        if (tracks.contains(track)) {
            tracks.remove(track)
        }
        tracks.add(track)
        trackStorage.saveSearchHistory(tracks)
    }

    override fun clearTheHistory() {
        trackStorage.clearHistory()
    }
}
