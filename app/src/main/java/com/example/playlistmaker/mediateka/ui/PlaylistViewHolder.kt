package com.example.playlistmaker.mediateka.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewMediatekaBinding
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.util.changeTheEndingOfTheWord

class PlaylistViewHolder(binding: PlaylistViewMediatekaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val image: ImageView = binding.coverPlaylist
    private val name: TextView = binding.playlistName
    private val tracksCount: TextView = binding.countTracks

    fun bind(model: Playlist) {
        name.text = model.playlistName
        val trackCount = "${model.tracksCount} ${changeTheEndingOfTheWord(model.tracksCount)}"
        tracksCount.text = trackCount

        Glide.with(itemView)
            .load(model.urlImage)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_playlist_placeholder)
            .centerCrop()
            .into(image)
        image.clipToOutline = true
    }
}
