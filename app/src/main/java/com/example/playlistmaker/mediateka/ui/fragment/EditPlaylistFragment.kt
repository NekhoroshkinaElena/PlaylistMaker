package com.example.playlistmaker.mediateka.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateka.ui.models.EditPlaylistState
import com.example.playlistmaker.mediateka.ui.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : NewPlaylistFragment() {

    override val playlistViewModel: EditPlaylistViewModel by viewModel() {
        parametersOf(requireArguments().getInt(PLAYLIST_KEY))
    }
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var playlistCoverUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbarNewPlaylist.title = requireActivity().getString(R.string.edit)
        binding.createPlaylist.text = requireActivity().getString(R.string.save)
        playlistViewModel.observeStateEditPlaylist().observe(viewLifecycleOwner) {
            when (it) {
                is EditPlaylistState.Content -> showData(it)
            }
        }

        binding.inputName.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                playlistName = text.toString()
                binding.createPlaylist.isEnabled = text.isNotEmpty() && text.isNotBlank()
            }
        }

        binding.inputDescription.doOnTextChanged { text, start, before, count ->
            playlistDescription = text.toString()
        }

        binding.createPlaylist.setOnClickListener {
            playlistViewModel.editPlaylist(playlistCoverUri, playlistName, playlistDescription)
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(enabled = true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    playlistCoverUri = uri
                    binding.addPhoto.setImageURI(uri)
                    binding.addPhoto.clipToOutline = true
                }
            }

        binding.addPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun showData(editPlaylistState: EditPlaylistState.Content) {
        binding.inputDescription.setText(editPlaylistState.playlistDescription)
        binding.inputName.setText(editPlaylistState.playlistName)

        if (editPlaylistState.imageUrl != null){
            binding.addPhoto.setImageURI(editPlaylistState.imageUrl)
            binding.addPhoto.clipToOutline = true
        }
    }

    companion object {
        private const val PLAYLIST_KEY = "edit_playlist_key"
        fun createArgs(idPlaylist: Int): Bundle {
            return bundleOf(PLAYLIST_KEY to idPlaylist)
        }
    }
}
