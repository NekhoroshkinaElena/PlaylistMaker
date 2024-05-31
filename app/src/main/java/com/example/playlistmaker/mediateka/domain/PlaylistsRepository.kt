package com.example.playlistmaker.mediateka.domain

import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun addPlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylist(track: Track, playlist: Playlist)

    suspend fun addTrackInPlaylist(track: Track)
}
