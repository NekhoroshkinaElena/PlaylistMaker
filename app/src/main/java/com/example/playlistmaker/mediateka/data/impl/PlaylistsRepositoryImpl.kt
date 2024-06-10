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

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return converter.playlistEntityToPlaylist(
            playlistDb.playlistDao().getPlaylistById(playlistId)
        )
    }

    override suspend fun updatePlaylistAddTrack(track: Track, playlist: Playlist) {
        playlist.tracksIds.add(track.trackId)
        playlist.tracksCount += 1

        playlistDb.playlistDao()
            .updatePlaylist(
                converter.playlistToPlaylistEntity(playlist)
            )
        addTrackInPlaylist(track)
    }

    override suspend fun updatePlaylistDeleteTrack(trackId: Int, playlist: Playlist) {
        playlist.tracksIds.remove(trackId)
        playlist.tracksCount = playlist.tracksIds.size
        playlistDb.playlistDao().updatePlaylist(
            converter.playlistToPlaylistEntity(playlist)
        )
        val list = mutableListOf<Int>()
        playlistDb.playlistDao().getAllTracksInPlaylists()
            .map {
                list.addAll(converter.idsStringToList(it))
            }
        if (!list.contains(trackId)) {
            playlistDb.trackPlaylistDao()
                .deleteTrackFromPlaylistById(trackId)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDb.playlistDao().updatePlaylist(converter.playlistToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistDb.playlistDao().deletePlaylist(playlistId)
        deleteUnusedTracks()
    }

    override suspend fun addTrackInPlaylist(track: Track) {
        playlistDb.trackPlaylistDao().insertTrack(converter.trackToTrackPlaylistEntity(track))
    }

    override suspend fun getTrackById(trackId: Int): Track {
        return converter.trackPlaylistEntityToTrack(
            playlistDb.trackPlaylistDao().getTrackById(trackId)
        )
    }

    private suspend fun deleteUnusedTracks() {
        val listAllTracksInPlaylists: MutableList<Int> = mutableListOf()
        playlistDb.playlistDao().getAllTracksInPlaylists().map { res ->
            listAllTracksInPlaylists.addAll(converter.idsStringToList(res))
        }
        val listTrack = playlistDb.trackPlaylistDao().getAllIdsTrack()
        listTrack.map {
            if (!listAllTracksInPlaylists.contains(it)) {
                playlistDb.trackPlaylistDao().deleteTrackFromPlaylistById(it)
            }
        }
    }
}
