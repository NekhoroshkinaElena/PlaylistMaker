package com.example.playlistmaker.mediateka.domain

import android.net.Uri
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylist(track: Track, playlist: Playlist)

    suspend fun addTrackInPlaylist(track: Track)

    fun saveImageToPrivateStorage(uri: Uri): String

    fun getImageFromPrivateStorage(imageName: String): Uri
}
