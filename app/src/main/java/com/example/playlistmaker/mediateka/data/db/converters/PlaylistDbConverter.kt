package com.example.playlistmaker.mediateka.data.db.converters

import androidx.core.net.toUri
import com.example.playlistmaker.mediateka.data.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.data.db.entity.TrackPlaylistEntity
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(private val gson: Gson) {

    fun playlistToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        val listTrackId: String = gson.toJson(playlist.tracksIds, ArrayList<Int>()::class.java)
        return PlaylistEntity(
            playlist.id,
            playlist.playlistName,
            playlist.description,
            playlist.urlImage.toString(),
            listTrackId,
            playlist.tracksCount
        )
    }

    fun idsStringToList(string: String): List<Int>{
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        val trackIds = gson.fromJson<ArrayList<Int>>(string, type)
        return trackIds
    }

    fun playlistEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        val type = object : TypeToken<ArrayList<Int>>() {}.type
        val trackIds = gson.fromJson<ArrayList<Int>>(playlistEntity.tracksIds, type)
        return Playlist(
            playlistEntity.id,
            playlistEntity.playlistName,
            playlistEntity.description,
            if (playlistEntity.urlImage=="null") null else playlistEntity.urlImage.toUri(),
            trackIds,
            playlistEntity.tracksCount
        )
    }

    fun trackToTrackPlaylistEntity(track: Track): TrackPlaylistEntity {
        return TrackPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.releaseDate,
            track.primaryGenreName,
            track.collectionName,
            track.country,
            track.previewUrl
        )
    }

    fun trackPlaylistEntityToTrack(trackPlaylistEntity: TrackPlaylistEntity): Track {
        return Track(
            trackPlaylistEntity.trackId,
            trackPlaylistEntity.trackName,
            trackPlaylistEntity.artistName,
            trackPlaylistEntity.trackTimeMillis,
            trackPlaylistEntity.artworkUrl100,
            trackPlaylistEntity.releaseDate,
            trackPlaylistEntity.primaryGenreName,
            trackPlaylistEntity.collectionName,
            trackPlaylistEntity.country,
            trackPlaylistEntity.previewUrl
        )
    }
}
