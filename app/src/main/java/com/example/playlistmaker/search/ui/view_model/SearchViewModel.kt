package com.example.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult
import com.example.playlistmaker.search.ui.models.SearchScreenState

class SearchViewModel(private val tracksInteractor: TrackInteractor) : ViewModel(){

    private var screenStateLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Unfocused)

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    fun getScreenStateLiveData(): LiveData<SearchScreenState> = screenStateLiveData

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun updateSearch(){
        latestSearchText?.let { searchRequest(it) }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchScreenState.Loading)

            tracksInteractor.searchTrack(newSearchText, object : TrackInteractor.TracksConsumer {
                override fun consume(tracksListResult: TracksListResult) {
                    val tracks = mutableListOf<Track>()
                    if (tracksListResult.tracks.isNotEmpty()) {
                        tracks.addAll(tracksListResult.tracks)
                    }
                    when (tracksListResult.codeResponse) {
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
            })
        }
    }

    private fun renderState(state: SearchScreenState) {
        screenStateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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
