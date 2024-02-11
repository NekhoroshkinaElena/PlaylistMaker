package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackRequest
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksListResponse

class TracksRepositoryImpl(private val requester: Requester) : TrackRepository {
    override fun searchTracks(text: String): TracksListResponse {
        val response = requester.doRequest(TrackRequest(text))

        if (response.resultCode == 200) {
            val list: List<Track> = (response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.collectionName,
                    it.country,
                    it.previewUrl
                )
            }
            return TracksListResponse(list, response.resultCode)

        } else {
            return TracksListResponse(emptyList(), response.resultCode)
        }
    }
}
