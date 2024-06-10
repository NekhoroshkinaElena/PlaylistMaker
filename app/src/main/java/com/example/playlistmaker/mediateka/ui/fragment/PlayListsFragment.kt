package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.mediateka.ui.PlaylistAdapter
import com.example.playlistmaker.mediateka.ui.models.PlaylistsState
import com.example.playlistmaker.mediateka.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment : Fragment() {

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistsBinding

    private val playlistClickListener = object : PlaylistAdapter.PlaylistClickListener {
        override fun onPlaylistClick(playlistId: Int) {
            findNavController().navigate(
                R.id.action_libraryFragment_to_playlistFragment2,
                PlaylistFragment.createArgs(playlistId)
            )
        }
    }

    private val playlistAdapter = PlaylistAdapter(playlistClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Content -> {
                    showContent(it.playList)
                }

                is PlaylistsState.Empty -> showEmpty(it.playlist)
            }
        }

        binding.rvListPlaylists.adapter = playlistAdapter

        binding.buttonNewPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment2)
        }
    }

    private fun showEmpty(playList: List<Playlist>) {
        binding.message.isVisible = true
        binding.placeholderImage.isVisible = true
        playlistAdapter.playlist = playList as ArrayList<Playlist>
        playlistAdapter.notifyDataSetChanged()
    }

    private fun showContent(playList: List<Playlist>) {
        binding.message.isVisible = false
        binding.placeholderImage.isVisible = false
        playlistAdapter.playlist = playList as ArrayList<Playlist>
        playlistAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): Fragment {
            return PlayListsFragment()
        }
    }
}
