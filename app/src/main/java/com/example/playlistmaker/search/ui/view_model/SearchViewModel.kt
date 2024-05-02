package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TrackInteractor) : ViewModel() {

    private var screenStateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.Unfocused)

    private var latestSearchText: String? = null

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchRequest(changedText)
        }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun getScreenStateLiveData(): LiveData<SearchScreenState> = screenStateLiveData

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun updateSearch() {
        latestSearchText?.let { searchRequest(it) }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchScreenState.Loading)

            viewModelScope.launch {
                tracksInteractor
                    .searchTrack(newSearchText)
                    .collect { result ->
                        processResult(result)
                    }
            }
        }
    }

    private fun processResult(result: TracksListResult?) {
        val tracks = mutableListOf<Track>()
        if (result?.tracks != null) {
            tracks.addAll(result.tracks)
        }
        when (result?.codeResponse) {
            200 -> {
                if (tracks.isNotEmpty()) {
                    renderState(SearchScreenState.Content(tracks))
                } else {
                    renderState(SearchScreenState.Empty)
                }
            }

            404 -> renderState(SearchScreenState.Empty)
            -1 -> renderState(SearchScreenState.Error)
            else -> renderState(SearchScreenState.Error)
        }
    }

    private fun renderState(state: SearchScreenState) {
        screenStateLiveData.postValue(state)
    }

    fun addTrackToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
        if (screenStateLiveData.value is SearchScreenState.History) {
            screenStateLiveData.postValue(SearchScreenState.History(tracksInteractor.getSearchHistory()))
        }
    }

    fun clearHistory() {
        tracksInteractor.clearTheHistory()
        screenStateLiveData.postValue(SearchScreenState.Unfocused)
    }

    fun onFocused() {
        onCleared()
        val list = tracksInteractor.getSearchHistory()
        if (list.isEmpty()) {
            screenStateLiveData.postValue(SearchScreenState.Unfocused)
        } else {
            screenStateLiveData.postValue(SearchScreenState.History(list))
        }
    }
}
