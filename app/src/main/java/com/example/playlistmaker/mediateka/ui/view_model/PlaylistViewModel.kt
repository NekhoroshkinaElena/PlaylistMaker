package com.example.playlistmaker.mediateka.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.ui.models.PlaylistState
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    init {
        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect { result ->
                if (result.isEmpty()) {
                    stateLiveData.postValue(PlaylistState.Empty)
                } else {
                    stateLiveData.postValue(PlaylistState.Content(result))
                }
            }
        }
    }
}
