package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistViewPlayerBinding
import com.example.playlistmaker.mediateka.domain.model.Playlist

class TrackPlaylistAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<TrackPlaylistViewHolder>() {

    var playlist = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackPlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)

        return TrackPlaylistViewHolder(PlaylistViewPlayerBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return playlist.size
    }

    override fun onBindViewHolder(holder: TrackPlaylistViewHolder, position: Int) {
        val playlist: Playlist = playlist[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(playlist)
        }
    }

    interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}
