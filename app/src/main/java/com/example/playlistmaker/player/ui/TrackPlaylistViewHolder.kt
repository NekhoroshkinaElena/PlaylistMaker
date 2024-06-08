package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewPlayerBinding
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.util.changeTheEndingOfTheWord

class TrackPlaylistViewHolder(binding: PlaylistViewPlayerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val playlistCover = binding.playlistCover
    private val playlistName = binding.playlistName
    private val tracksCount = binding.trackCount

    fun bind(model: Playlist) {
        playlistName.text = model.playlistName
        val trackCount = "${model.tracksCount} ${changeTheEndingOfTheWord(model.tracksCount)}"
        tracksCount.text = trackCount

        Glide.with(itemView)
            .load(model.urlImage)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .into(playlistCover)

        playlistCover.clipToOutline = true
    }
}
