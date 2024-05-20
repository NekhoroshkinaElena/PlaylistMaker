package com.example.playlistmaker.mediateka.data.db.converters

import com.example.playlistmaker.mediateka.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.model.Track

class TrackDbConverter {

    fun trackToTrackEntity(track: Track): TrackEntity {
        return TrackEntity(
            0,
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

    fun trackEntityToTrack(track: TrackEntity): Track {
        return Track(
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
}
