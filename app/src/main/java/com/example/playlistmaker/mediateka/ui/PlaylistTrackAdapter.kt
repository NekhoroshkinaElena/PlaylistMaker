package com.example.playlistmaker.mediateka.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TrackViewHolder

class PlaylistTrackAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)

        return TrackViewHolder(TrackViewBinding.inflate(view, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track: Track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(track)
        }

        holder.itemView.setOnLongClickListener {
            clickListener.onTrackLongClick(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    interface TrackClickListener {
        fun onTrackClick(track: Track)

        fun onTrackLongClick(track: Track): Boolean
    }
}
