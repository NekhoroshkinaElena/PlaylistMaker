package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.mediateka.ui.PlaylistTrackAdapter
import com.example.playlistmaker.mediateka.ui.models.PlaylistState
import com.example.playlistmaker.mediateka.ui.view_model.PlaylistViewModel
import com.example.playlistmaker.player.ui.fragment.TrackFragment
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private var playlistId: Int? = null

    private val viewModel: PlaylistViewModel by viewModel {
        parametersOf(playlistId)
    }

    private lateinit var binding: FragmentPlaylistBinding
    private var isClickAllowed = true
    private lateinit var onTrackClickDebounce: (Track) -> Unit
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<ConstraintLayout>


    private val trackClickListener = object : PlaylistTrackAdapter.TrackClickListener {
        override fun onTrackClick(track: Track) {
            if (isClickAllowed) {
                isClickAllowed = false

                findNavController().navigate(
                    R.id.action_playlistFragment2_to_trackFragment,
                    TrackFragment.createArgs(track)
                )
                onTrackClickDebounce(track)
            }
        }

        override fun onTrackLongClick(track: Track): Boolean {
            showConfirmDialogDeleteTrack(track)
            return true
        }
    }

    private val trackAdapter = PlaylistTrackAdapter(trackClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = requireArguments().getInt(PLAYLIST_KEY)
        binding.rvListPlaylists.adapter = trackAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.Content -> showPlaylist(it)
                is PlaylistState.Delete -> findNavController().navigateUp()
            }
        }

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            requireActivity().lifecycleScope, false
        ) { isClickAllowed = true }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val bottomSheetContainerTrack = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainerTrack)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val bottomSheetContainerMenu = binding.bottomSheetMenu
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(bottomSheetContainerMenu)
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
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

        binding.share.setOnClickListener {
            sharePlaylist()
        }

        binding.shareText.setOnClickListener {
            sharePlaylist()
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.menu.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.deletePlaylist.setOnClickListener {
            showConfirmDialogDeletePlaylist()
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.editInfo.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment2_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlistId!!)
            )
        }
    }


    private fun showPlaylist(playlist: PlaylistState.Content) {
        binding.playlistName.text = playlist.playlistName
        if (playlist.playlistDescription?.isNotEmpty() == true) {
            binding.playlistDescription.visibility = View.VISIBLE
            binding.playlistDescription.text = playlist.playlistDescription
        } else {
            binding.playlistDescription.visibility = View.GONE
        }
        Glide.with(this)
            .load(playlist.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .into(binding.coverPlaylist)

        if (!playlist.listTracks.isNullOrEmpty()) {
            trackAdapter.tracks = playlist.listTracks as ArrayList<Track>
            trackAdapter.notifyDataSetChanged()
        }

        binding.playlistDuration.text = playlist.playlistDuration
        binding.playlistTracksCount.text = playlist.playlistCountTrack

        if (trackAdapter.tracks.isEmpty()) {
            binding.placeholderMessage.visibility = View.VISIBLE
        } else {
            binding.placeholderMessage.visibility = View.GONE
        }

        Glide.with(this)
            .load(playlist.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.ic_placeholder_track)
            .centerCrop()
            .into(binding.playlistCover)
        binding.playlistCover.clipToOutline = true
        binding.playlistNameItem.text = playlist.playlistName
        binding.trackCount.text = playlist.playlistCountTrack
    }

    private fun showConfirmDialogDeleteTrack(track: Track) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle(R.string.delete_track)
            .setMessage(R.string.confirm_delete_track)
            .setNeutralButton(R.string.cancel) { _, _ ->
            }
            .setNegativeButton(R.string.delete) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track.trackId)
            }.show()
    }

    private fun showConfirmDialogDeletePlaylist() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle(R.string.deletePlaylist)
            .setMessage(R.string.confirm_delete_playlist)
            .setNeutralButton(R.string.cancel) { _, _ ->
        }
            .setNegativeButton(R.string.delete) { _, _ ->
                viewModel.deletePlaylist()
            }.show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.updatePlaylist()
    }

    private fun sharePlaylist() {
        if (trackAdapter.tracks.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                requireActivity().getString(R.string.error_empty_playlist),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.sharePlaylist()
        }
    }

    companion object {
        private const val PLAYLIST_KEY = "playlist_key"
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        fun createArgs(idPlaylist: Int): Bundle {
            return bundleOf(PLAYLIST_KEY to idPlaylist)
        }
    }
}
