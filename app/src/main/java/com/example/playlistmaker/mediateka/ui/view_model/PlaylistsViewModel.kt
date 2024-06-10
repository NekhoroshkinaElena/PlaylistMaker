package com.example.playlistmaker.mediateka.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.ui.models.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    init {
        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect { result ->
                if (result.isEmpty()) {
                    stateLiveData.postValue(PlaylistsState.Empty(result))
                } else {
                    stateLiveData.postValue(PlaylistsState.Content(result))
                }
            }
        }
    }
}
