package com.example.playlistmaker.mediateka.data.impl

import com.example.playlistmaker.mediateka.data.db.FavoritesTracksDatabase
import com.example.playlistmaker.mediateka.data.db.converters.TrackDbConverter
import com.example.playlistmaker.mediateka.domain.FavoritesTracksRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoritesTrackRepositoryImpl(
    private val database: FavoritesTracksDatabase,
    private val converter: TrackDbConverter
) : FavoritesTracksRepository {
    override suspend fun addTrack(track: Track) {
        database.trackDao().insertTrack(
            converter.trackToTrackEntity(
                track
            )
        )
    }

    override suspend fun deleteTrack(track: Track) {
        database.trackDao().deleteTrack(track.trackId)
    }

    override fun getIdsTracks(): Flow<List<Int>> {
        return database.trackDao().getIdsTracks()
    }

    override fun getAllFavoritesTrack(): Flow<List<Track>> {
        return database.trackDao().getAllTrack().map { result ->
            result.map { converter.trackEntityToTrack(it) }
        }
    }
}
