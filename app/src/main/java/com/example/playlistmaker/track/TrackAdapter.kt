package com.example.playlistmaker.track

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.history.SearchHistory

const val TRACK_KEY = "track"

class TrackAdapter(var results: ArrayList<Track>) :

    RecyclerView.Adapter<TrackViewHolder>() {

    private lateinit var searchHistory: SearchHistory

    fun setSearchHistory(searchHistory: SearchHistory) {
        this.searchHistory = searchHistory
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
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, track)
            context.startActivity(intent)

            searchHistory.addTrackToHistory(results[position])

        }
    }

    override fun getItemCount(): Int {
        return results.size
    }
}
