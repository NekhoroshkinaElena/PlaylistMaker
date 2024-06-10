package com.example.playlistmaker.player.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.mediateka.ui.models.PlaylistsState
import com.example.playlistmaker.player.ui.TrackPlaylistAdapter
import com.example.playlistmaker.player.ui.models.TrackScreenState
import com.example.playlistmaker.player.ui.view_model.TrackViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.dpToPx
import com.example.playlistmaker.util.getYearFromString
import com.example.playlistmaker.util.millisecondToMinutesAndSeconds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val IMAGE_CORNER_RADIUS = 8f

class TrackFragment : Fragment() {
    private lateinit var binding: FragmentAudioPlayerBinding

    private var track: Track? = null

    private val viewModel: TrackViewModel by viewModel {
        parametersOf(track)
    }

    private val playlistClickListener = object : TrackPlaylistAdapter.PlaylistClickListener {
        override fun onPlaylistClick(playlist: Playlist) {
            viewModel.addTrackToPlaylist(track, playlist)
        }
    }

    private var adapter: TrackPlaylistAdapter = TrackPlaylistAdapter(playlistClickListener)
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarAudioPlayerScreen.setNavigationOnClickListener() {
            findNavController().navigateUp()
        }

        track = requireArguments().getParcelable(TRACK_KEY)

        val trackCoverUrl = track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        val trackTime = track?.trackTimeMillis ?: ""
        val album = track?.collectionName ?: ""

        Glide.with(this)
            .load(trackCoverUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(IMAGE_CORNER_RADIUS, requireActivity())))
            .into(binding.trackCover)

        binding.trackName.text = track?.trackName
        binding.artistName.text = track?.artistName
        binding.trackTime.text = millisecondToMinutesAndSeconds(trackTime)
        binding.trackDurationValue.text = millisecondToMinutesAndSeconds(trackTime)
        binding.releaseDateValue.text = getYearFromString(track?.releaseDate)
        binding.countryValue.text = track?.country
        binding.genreValue.text = track?.primaryGenreName
        binding.rvListPlaylists.adapter = adapter

        val bottomSheetContainer = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        if (album.isNotBlank()) {
            binding.collectionNameValue.text = album
            binding.collectionName.isVisible = true
            binding.collectionNameValue.isVisible = true
        }

        viewModel.preparePlayer()

        binding.addTrack.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.playTrack.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.addToFavorites.setOnClickListener {
            viewModel.onClickFavorite()
        }

        binding.buttonNewPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_trackFragment_to_newPlaylistFragment)
        }

        viewModel.getScreenStateMediaPlayer().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getStateIsFavorite().observe(viewLifecycleOwner) {
            renderIsFavorite(it)
        }

        viewModel.getStatePlaylist().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Content -> {
                    showContent(it.playList)
                }

                else -> {}
            }
        }

        viewModel.getStateAddTrack().observe(viewLifecycleOwner) {
            when (it) {
                false -> {
                    Toast.makeText(
                        requireActivity(),
                        "Трек уже присутствует в плейлисте",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.deleteValueStateAddTrack()
                }

                true -> showSuccess()
                else -> {}
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
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
        requireActivity().recreate()
    }

    private fun showContent(playList: List<Playlist>) {
        adapter.playlist = playList as ArrayList<Playlist>
        adapter.notifyDataSetChanged()
    }

    private fun showSuccess() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        Toast.makeText(
            requireActivity(),
            "Трек добавлен в плейлист",
            Toast.LENGTH_SHORT
        ).show()
        adapter.notifyDataSetChanged()
        viewModel.deleteValueStateAddTrack()
    }

    companion object {
        const val TRACK_KEY = "track"

        fun createArgs(track: Track): Bundle =
            bundleOf(
                TRACK_KEY to track
            )
    }
}
