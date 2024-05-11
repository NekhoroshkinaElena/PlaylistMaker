package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.Requester
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.TrackRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val requester: Requester) : TrackRepository {
    override fun searchTracks(text: String): Flow<TracksListResult?> = flow {
        val response = requester.doRequest(TrackRequest(text))
        if (response.resultCode == 200) {
            with(response as TrackResponse) {
                val data = results.map {
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
                emit(TracksListResult(data, response.resultCode))
            }
        } else {
            emit(TracksListResult(emptyList(), response.resultCode))
        }
    }
}
