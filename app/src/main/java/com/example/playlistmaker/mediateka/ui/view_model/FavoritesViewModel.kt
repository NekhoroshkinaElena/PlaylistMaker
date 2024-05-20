package com.example.playlistmaker.mediateka.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.FavoritesTracksInteractor
import com.example.playlistmaker.mediateka.ui.models.FavoriteState
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesTracksInteractor: FavoritesTracksInteractor,
    private val trackInteractor: TrackInteractor
) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    init {
        viewModelScope.launch {
            favoritesTracksInteractor
                .getAllFavoritesTrack()
                .collect { result ->
                    if (result.isEmpty()) {
                        stateLiveData.postValue(FavoriteState.Empty)
                    } else {
                        stateLiveData.postValue(FavoriteState.Content(result))
                    }
                }
        }
    }

    fun addTrackToHistory(track: Track) {
        trackInteractor.addTrackToHistory(track)
    }
}
