package com.example.playlistmaker.mediateka.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewMediatekaBinding
import com.example.playlistmaker.mediateka.domain.model.Playlist

class PlaylistAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlist = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)

        return PlaylistViewHolder(PlaylistViewMediatekaBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist: Playlist = playlist[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(playlist.id)
        }
    }

    interface PlaylistClickListener {
        fun onPlaylistClick(playlistId: Int)
    }
}
