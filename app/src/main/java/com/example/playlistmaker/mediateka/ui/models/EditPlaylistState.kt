package com.example.playlistmaker.mediateka.ui.models

import android.net.Uri

sealed class EditPlaylistState {
    data class Content(
        val imageUrl: Uri?,
        val playlistName: String,
        val playlistDescription: String?
    ) : EditPlaylistState()
}
