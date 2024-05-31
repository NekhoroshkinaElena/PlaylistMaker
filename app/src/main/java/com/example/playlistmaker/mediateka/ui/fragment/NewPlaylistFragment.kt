package com.example.playlistmaker.mediateka.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.mediateka.ui.models.NewPlaylistState
import com.example.playlistmaker.mediateka.ui.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding
    private val playlistViewModel: NewPlaylistViewModel by viewModel()
    private var playlistName: String = ""
    private var playlistDescription: String = ""
    private var playlistCoverUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputName.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                playlistName = text.toString()
                binding.createPlaylist.isEnabled = text.isNotEmpty() && text.isNotBlank()
            }
        }

        binding.inputDescription.doOnTextChanged { text, start, before, count ->
            playlistDescription = text.toString()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    playlistCoverUri = uri
                    binding.addPhoto.setImageURI(uri)
                    binding.addPhoto.clipToOutline = true
                } else {
                }
            }

        binding.addPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is NewPlaylistState.Success -> {
                    Toast.makeText(
                        requireActivity(),
                        "Плейлист $playlistName создан",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }

                else -> {}
            }
        }

        binding.createPlaylist.setOnClickListener {
            playlistViewModel.addPlaylist(playlistName, playlistDescription, playlistCoverUri)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (playlistCoverUri != null || playlistName.isNotEmpty() || playlistDescription.isNotEmpty()) {
                    showConfirmDialog()
                } else {
                    findNavController().navigateUp()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.toolbarNewPlaylist.setNavigationOnClickListener {
            if (playlistCoverUri != null || playlistName.isNotEmpty() || playlistDescription.isNotEmpty()) {
                showConfirmDialog()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { _, _ ->
            }
            .setNegativeButton("Завершить") { _, _ ->
                findNavController().navigateUp()
            }.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (playlistCoverUri != null) {
            outState.putString("playlistCover", playlistCoverUri.toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.addPhoto.setImageURI(savedInstanceState.getString("playlistCover")?.toUri())
            binding.addPhoto.clipToOutline = true
            playlistCoverUri = savedInstanceState.getString("playlistCover")?.toUri()
        }

    }
}
