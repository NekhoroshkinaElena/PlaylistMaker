package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesTracksRepository {
    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getIdsTracks():Flow<List<Int>>

    fun getAllFavoritesTrack(): Flow<List<Track>>
}
