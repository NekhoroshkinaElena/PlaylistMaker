package com.example.playlistmaker.search.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.millisecondToMinutesAndSeconds

class TrackViewHolder(binding: TrackViewBinding) : RecyclerView.ViewHolder(binding.root) {

    private val name: TextView
    private val artist: TextView
    private val time: TextView
    private val image: ImageView

    init {
        name = binding.trackName
        artist = binding.artistName
        time = binding.trackTime
        image = binding.imageTrack
    }

    fun bind(model: Track) {
        name.text = model.trackName
        artist.text = model.artistName
        time.text = millisecondToMinutesAndSeconds(model.trackTimeMillis)
        setImage(model.artworkUrl100)
    }

    private fun setImage(url: String?) {
        Glide.with(itemView)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(4))
            .into(image)
    }
}
