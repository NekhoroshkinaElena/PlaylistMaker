package com.example.playlistmaker.track

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.history.SearchHistory
import com.example.playlistmaker.util.getYearFromString

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
            intent.putExtra("trackCover", track.artworkUrl100)
            intent.putExtra("trackName", track.trackName)
            intent.putExtra("artistName", track.artistName)
            intent.putExtra("trackTime", track.trackTimeMillis)
            intent.putExtra("releaseDate", getYearFromString(track.releaseDate))
            intent.putExtra("country", track.country)
            intent.putExtra("genre", track.primaryGenreName)
            intent.putExtra("album", track.collectionName)

            context.startActivity(intent)

            searchHistory.addTrackToHistory(results[position])

        }
    }

    override fun getItemCount(): Int {
        return results.size
    }
}
