package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesTracksInteractor {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    suspend fun getIdsTracks():Flow<List<Int>>

    suspend fun getAllFavoritesTrack(): Flow<List<Track>>
}
