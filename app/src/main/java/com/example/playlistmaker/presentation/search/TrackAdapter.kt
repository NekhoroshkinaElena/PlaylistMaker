package com.example.playlistmaker.presentation.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.player.AudioPlayerActivity

const val TRACK_KEY = "track"

class TrackAdapter(var results: ArrayList<Track>, val clickDebounce: () -> Boolean) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private lateinit var trackInteractor: TrackInteractor

    fun setSearchHistory(trackInteractor: TrackInteractor) {
        this.trackInteractor = trackInteractor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.track_view,
                parent, false
            )
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track: Track = results[position]
        holder.bind(track)

        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                val intent = Intent(context, AudioPlayerActivity::class.java)
                intent.putExtra(TRACK_KEY, track)
                context.startActivity(intent)

                trackInteractor.addTrackToHistory(results[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }
}