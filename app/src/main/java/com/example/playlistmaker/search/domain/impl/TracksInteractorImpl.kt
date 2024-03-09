package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.TrackStorage
import com.example.playlistmaker.search.domain.model.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val trackRepository: TrackRepository,
    private val trackStorage: TrackStorage
) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(text: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            val res = trackRepository.searchTracks(text)
            consumer.consume(res)
        }
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
