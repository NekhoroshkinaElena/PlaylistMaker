package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addPlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistById(playlistId: Int): Playlist

    suspend fun updatePlaylistAddTrack(track: Track, playlist: Playlist)

    suspend fun updatePlaylistDeleteTrack(trackId: Int, playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun addTrackInPlaylist(track: Track)

    suspend fun getTrackById(trackId: Int): Track
}
