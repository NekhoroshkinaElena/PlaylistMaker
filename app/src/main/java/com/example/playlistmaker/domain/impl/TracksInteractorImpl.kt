package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TrackStorage
import com.example.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val trackRepository: TrackRepository,
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
