package com.example.playlistmaker.mediateka.data.impl

import com.example.playlistmaker.mediateka.data.db.PlaylistsDatabase
import com.example.playlistmaker.mediateka.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.mediateka.domain.PlaylistsRepository
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val playlistDb: PlaylistsDatabase,
    private val converter: PlaylistDbConverter
) : PlaylistsRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDb.playlistDao().insertPlaylist(converter.playlistToPlaylistEntity(playlist))
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDb.playlistDao().getAllPlaylist().map { listPlaylists ->
            listPlaylists.map { playlist ->
                converter.playlistEntityToPlaylist(playlist)
            }
        }
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        playlist.tracksIds.add(track.trackId)
        playlist.tracksCount += 1

        playlistDb.playlistDao()
            .updatePlaylist(
                converter.playlistToPlaylistEntity(playlist)
            )
        addTrackInPlaylist(track)
    }

    override suspend fun addTrackInPlaylist(track: Track) {
        playlistDb.trackPlaylistDao().insertTrack(converter.trackToTrackPlaylistEntity(track))
    }
}
