package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult
import com.example.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTracks(text: String): TracksListResult
//    fun searchTracks(text: String): Resource<List<Track>>
}
