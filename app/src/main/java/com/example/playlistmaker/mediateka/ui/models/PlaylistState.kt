package com.example.playlistmaker.mediateka.ui.models

import android.net.Uri
import com.example.playlistmaker.search.domain.model.Track

sealed class PlaylistState {
    data class Content(
        val imageUrl: Uri?,
        val playlistName: String,
        val playlistDescription: String?,
        val playlistDuration: String?,
        val playlistCountTrack: String?,
        val listTracks: List<Track>?
    ) : PlaylistState()

    data object Delete : PlaylistState()
}
