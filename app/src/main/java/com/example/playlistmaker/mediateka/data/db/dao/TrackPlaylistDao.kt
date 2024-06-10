package com.example.playlistmaker.mediateka.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.mediateka.data.db.entity.TrackPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist WHERE trackId IN (:tracksIds)")
    fun getAllTracksInPlaylist(tracksIds: List<Int>): Flow<List<TrackPlaylistEntity>>

    @Query("SELECT * FROM track_in_playlist WHERE trackId = :playlistId")
    suspend fun getTrackById(playlistId: Int): TrackPlaylistEntity

    @Query("SELECT trackId FROM track_in_playlist")
    suspend fun getAllIdsTrack(): List<Int>

    @Query("DELETE FROM track_in_playlist WHERE trackId = :trackId")
    suspend fun deleteTrackFromPlaylistById(trackId: Int)
}
