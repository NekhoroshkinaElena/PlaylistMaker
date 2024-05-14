package com.example.playlistmaker.mediateka.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.mediateka.ui.models.FavoriteState
import com.example.playlistmaker.mediateka.ui.view_model.FavoritesViewModel
import com.example.playlistmaker.player.ui.activity.TrackActivity
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TRACK_KEY
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val aboutViewModel: FavoritesViewModel by viewModel()

    private lateinit var binding: FragmentFavoritesBinding

    private var isClickAllowed = true

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val trackClickListener = object : TrackAdapter.TrackClickListener {
        override fun onTrackClick(track: Track) {
            if (isClickAllowed) {
                isClickAllowed = false
                val intent = Intent(requireContext(), TrackActivity::class.java)
                intent.putExtra(TRACK_KEY, track)
                startActivity(intent)

                aboutViewModel.addTrackToHistory(track)
                onTrackClickDebounce(track)
            }
        }
    }

    private val trackAdapter = TrackAdapter(
        trackClickListener
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false
        ) {
            isClickAllowed = true
        }
        binding.rvFavoritesListTracks.adapter = trackAdapter

        aboutViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteState.Content -> showContent(it.tracks)

                is FavoriteState.Empty -> showEmpty()
            }
        }
    }

    private fun showEmpty() {
        binding.message.isVisible = true
        binding.placeholderImage.isVisible = true
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<Track>) {
        binding.message.isVisible = false
        binding.placeholderImage.isVisible = false
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        fun newInstance(): Fragment {
            return FavoritesFragment()
        }
    }
}
