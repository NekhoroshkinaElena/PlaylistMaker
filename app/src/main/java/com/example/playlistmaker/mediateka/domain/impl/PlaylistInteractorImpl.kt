package com.example.playlistmaker.mediateka.domain.impl

import android.net.Uri
import com.example.playlistmaker.mediateka.domain.ImageStorage
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.domain.PlaylistsRepository
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistsRepository,
    private val imageStorage: ImageStorage
) : PlaylistsInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun updatePlaylistAddTrack(track: Track, playlist: Playlist) {
        repository.updatePlaylistAddTrack(track, playlist)
    }

    override suspend fun updatePlaylistDeleteTrack(trackId: Int, playlist: Playlist) {
        repository.updatePlaylistDeleteTrack(trackId, playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun addTrackInPlaylist(track: Track) {
        repository.addTrackInPlaylist(track)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        repository.deletePlaylist(playlistId)
    }

    override suspend fun getTrackById(trackId: Int): Track {
        return repository.getTrackById(trackId)
    }

    override fun saveImageToPrivateStorage(uri: Uri): String {
        return imageStorage.saveImageToPrivateStorage(uri)
    }

    override fun getImageFromPrivateStorage(imageName: String): Uri {
        return imageStorage.getImageFromPrivateStorage(imageName)
    }
}
