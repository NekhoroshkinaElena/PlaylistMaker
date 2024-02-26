package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.history.TrackHistoryStorage
import com.example.playlistmaker.search.domain.TrackInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TracksListResult
import com.example.playlistmaker.search.ui.activity.SEARCH_HISTORY_PREFERENCES
import com.example.playlistmaker.search.ui.models.SearchState

class TracksSearchViewModel(application: Application) : AndroidViewModel(application) {

    private var screenStateLiveData = MutableLiveData<SearchState>(SearchState.Unfocused)

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private var tracksInteractor: TrackInteractor = Creator.provideTracksInteractor(
        TrackHistoryStorage(
            application.getSharedPreferences(
                SEARCH_HISTORY_PREFERENCES,
                AppCompatActivity.MODE_PRIVATE
            )
        ), application.applicationContext
    )

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as Application
                )
            }
        }
    }

    fun getScreenStateLiveData(): LiveData<SearchState> = screenStateLiveData

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
            renderState(SearchState.Loading)

            tracksInteractor.searchTrack(newSearchText, object : TrackInteractor.TracksConsumer {
                override fun consume(tracksListResult: TracksListResult) {
                    val tracks = mutableListOf<Track>()
                    if (tracksListResult.tracks.isNotEmpty()) {
                        tracks.addAll(tracksListResult.tracks)
                    }
                    when (tracksListResult.codeResponse) {
                        200 -> {
                            if (tracks.isNotEmpty()) {
                                renderState(SearchState.Content(tracks))
                            } else {
                                renderState(SearchState.Empty)
                            }
                        }
                        404 -> renderState(SearchState.Empty)
                        -1 -> renderState(SearchState.Error)
                        else -> renderState(SearchState.Error)
                    }
                }
            })
        }
    }

    private fun renderState(state: SearchState) {
        screenStateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun addTrackToHistory(track: Track) {
        tracksInteractor.addTrackToHistory(track)
        if (screenStateLiveData.value is SearchState.History) {
            screenStateLiveData.postValue(SearchState.History(tracksInteractor.getSearchHistory()))
        }
    }

    fun clearHistory() {
        tracksInteractor.clearTheHistory()
        screenStateLiveData.postValue(SearchState.Unfocused)
    }

    fun onFocused() {
        onCleared()
        val list = tracksInteractor.getSearchHistory()
        if (list.isEmpty()) {
            screenStateLiveData.postValue(SearchState.Unfocused)
        } else {
            screenStateLiveData.postValue(SearchState.History(list))
        }
    }
}
