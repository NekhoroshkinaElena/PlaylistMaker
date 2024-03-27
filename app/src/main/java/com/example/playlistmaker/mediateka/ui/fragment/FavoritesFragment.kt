package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.mediateka.ui.models.FavoriteState
import com.example.playlistmaker.mediateka.ui.view_model.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment {
            return FavoritesFragment()
        }
    }

    private val aboutViewModel: FavoritesViewModel by viewModel()

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aboutViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteState.Content -> {
                    TODO()
                }

                is FavoriteState.Empty -> showEmpty()
            }
        }
    }

    private fun showEmpty() {
        binding.message.isVisible = true
        binding.placeholderImage.isVisible = true
    }
}
