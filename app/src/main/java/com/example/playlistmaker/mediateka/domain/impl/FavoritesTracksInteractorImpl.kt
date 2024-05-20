package com.example.playlistmaker.mediateka.domain.impl

import com.example.playlistmaker.mediateka.domain.FavoritesTracksInteractor
import com.example.playlistmaker.mediateka.domain.FavoritesTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoritesTracksInteractorImpl(private val repository: FavoritesTracksRepository) :
    FavoritesTracksInteractor {
    override suspend fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        repository.deleteTrack(track)
    }

    override fun getIdsTracks(): Flow<List<Int>> {
        return repository.getIdsTracks()
    }

    override fun getAllFavoritesTrack(): Flow<List<Track>> {
        return repository.getAllFavoritesTrack()
    }
}
