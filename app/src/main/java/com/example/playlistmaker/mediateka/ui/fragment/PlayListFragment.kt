package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.mediateka.ui.models.PlaylistState
import com.example.playlistmaker.mediateka.ui.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment {
            return PlayListFragment()
        }
    }

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistBinding

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
                    TODO()
                }

                is PlaylistState.Empty -> showEmpty()
            }
        }
    }

    private fun showEmpty() {
        binding.message.isVisible = true
        binding.placeholderImage.isVisible = true
    }
}
