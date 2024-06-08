package com.example.playlistmaker.mediateka.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.mediateka.ui.models.NewPlaylistState
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val statePlaylistLiveData = MutableLiveData<NewPlaylistState>()
    fun observeState(): LiveData<NewPlaylistState> = statePlaylistLiveData

    fun addPlaylist(playlistName: String, playlistDescription: String, imageUri: Uri?) {
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(
                Playlist(
                    0,
                    playlistName,
                    playlistDescription,
                    if (imageUri != null) playlistsInteractor.getImageFromPrivateStorage(
                        playlistsInteractor.saveImageToPrivateStorage(imageUri)
                    ) else null,
                    mutableListOf<Int>(),
                    0
                )
            )
            statePlaylistLiveData.postValue(NewPlaylistState.Success)
        }
    }
}
