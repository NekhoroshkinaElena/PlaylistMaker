package com.example.playlistmaker.track

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.history.SearchHistory

class TrackAdapter(private val results: ArrayList<Track>) :
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
        holder.bind(results[position])
        holder.itemView.setOnClickListener { view ->
            Toast.makeText(
                view.context,
                R.string.go_to_the_track,
                Toast.LENGTH_SHORT
            ).show()

            searchHistory.addTrackToHistory(results[position])
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }
}
