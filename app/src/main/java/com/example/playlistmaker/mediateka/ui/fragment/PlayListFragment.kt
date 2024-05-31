package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.mediateka.ui.PlaylistAdapter
import com.example.playlistmaker.mediateka.ui.models.PlaylistState
import com.example.playlistmaker.mediateka.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistBinding

    private val playlistAdapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.Content -> {
                    showContent(it.playList)
                }

                is PlaylistState.Empty -> showEmpty()
            }
        }

        binding.rvListPlaylists.adapter = playlistAdapter

        binding.buttonNewPlayList.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment2)
        }
    }

    private fun showEmpty() {
        binding.message.isVisible = true
        binding.placeholderImage.isVisible = true
    }

    private fun showContent(playList: List<Playlist>) {
        binding.message.isVisible = false
        binding.placeholderImage.isVisible = false
        playlistAdapter.playlist = playList as ArrayList<Playlist>
        playlistAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): Fragment {
            return PlayListFragment()
        }
    }
}
