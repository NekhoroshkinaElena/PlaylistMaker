package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.Requester
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult

class TracksRepositoryImpl(private val requester: Requester) : TrackRepository {
    override fun searchTracks(text: String): TracksListResult {
        val response = requester.doRequest(TrackRequest(text))

        if (response.resultCode == 200) {
            val list: List<Track> = (response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName ?: "",
                    it.artistName ?: "",
                    it.trackTimeMillis ?: "",
                    it.artworkUrl100 ?: "",
                    it.releaseDate ?: "",
                    it.primaryGenreName ?: "",
                    it.collectionName ?: "",
                    it.country ?: "",
                    it.previewUrl ?: ""
                )
            }
            return TracksListResult(list, response.resultCode)

        } else {
            return TracksListResult(emptyList(), response.resultCode)
        }
    }
}
