package com.example.playlistmaker.player.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.models.TrackScreenState
import com.example.playlistmaker.player.ui.view_model.TrackViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TRACK_KEY
import com.example.playlistmaker.util.dpToPx
import com.example.playlistmaker.util.getYearFromString
import com.example.playlistmaker.util.millisecondToMinute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val IMAGE_CORNER_RADIUS = 8f

class TrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private var track: Track? = null

    private val viewModel: TrackViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAudioPlayerScreen.setNavigationOnClickListener() {
            finish()
        }

        track = intent.getParcelableExtra(TRACK_KEY)

        val trackCoverUrl = track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        val trackTime = track?.trackTimeMillis ?: ""
        val album = track?.collectionName ?: ""

        Glide.with(applicationContext)
            .load(trackCoverUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(IMAGE_CORNER_RADIUS, this)))
            .into(binding.trackCover)

        binding.trackName.text = track?.trackName
        binding.artistName.text = track?.artistName
        binding.trackTime.text = millisecondToMinute(trackTime)
        binding.trackDurationValue.text = millisecondToMinute(trackTime)
        binding.releaseDateValue.text = getYearFromString(track?.releaseDate)
        binding.countryValue.text = track?.country
        binding.genreValue.text = track?.primaryGenreName

        if (album.isNotBlank()) {
            binding.collectionNameValue.text = album
            binding.collectionName.isVisible = true
            binding.collectionNameValue.isVisible = true
        }

        viewModel.preparePlayer()

        binding.playTrack.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.addToFavorites.setOnClickListener {
            viewModel.onClickFavorite()
        }

        viewModel.getScreenStateMediaPlayer().observe(this) {
            render(it)
        }

        viewModel.getStateIsFavorite().observe(this) {
            renderIsFavorite(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun render(trackScreenState: TrackScreenState) {
        binding.playTrack.isEnabled = trackScreenState.isPlayButtonEnabled
        binding.trackTime.text = trackScreenState.currentPosition
        binding.playTrack.setImageResource(
            if (trackScreenState.isPlayButtonActive) R.drawable.ic_play else R.drawable.ic_button_pause
        )
    }

    private fun renderIsFavorite(isFavorite: Boolean) {
        binding.addToFavorites.setImageResource(
            if (isFavorite) R.drawable.ic_delete_from_favorites
            else R.drawable.ic_add_to_favorites
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.onChangeConfig()
        recreate()
    }
}
