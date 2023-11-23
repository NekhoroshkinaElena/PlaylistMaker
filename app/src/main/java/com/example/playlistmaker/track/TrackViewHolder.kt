package com.example.playlistmaker.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {

    private val name: TextView
    private val artist: TextView
    private val time: TextView
    private val image: ImageView

    init {
        name = parentView.findViewById(R.id.trackName)
        artist = parentView.findViewById(R.id.artistName)
        time = parentView.findViewById(R.id.trackTime)
        image = parentView.findViewById(R.id.imageTrack)
    }

    fun bind(model: Track) {
        name.text = model.trackName
        artist.text = model.artistName
        time.text = model.trackTime
        setImage(model.artworkUrl100)
    }

    private fun setImage(url: String?) {
        Glide.with(itemView)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(4))
            .into(image)
    }
}
