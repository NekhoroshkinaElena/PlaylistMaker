package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.FavoritesTracksInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.models.TrackScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.millisecondToMinute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackViewModel(
    private var track: Track?,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesTracksInteractor: FavoritesTracksInteractor,
) : ViewModel() {

    private val screenStateMediaPlayer =
        MutableLiveData<TrackScreenState>(TrackScreenState.Default())

    private val stateIsFavorite = MutableLiveData<Boolean>()

    private var timerJob: Job? = null

    init {
        mediaPlayerInteractor.preparePlayer()
        renderState(
            TrackScreenState.Prepared("00:00")
        )

        viewModelScope.launch {
            favoritesTracksInteractor.getIdsTracks().collect { ids ->
                stateIsFavorite.value = ids.contains(track?.trackId)
            }
        }
    }

    fun getScreenStateMediaPlayer(): LiveData<TrackScreenState> = screenStateMediaPlayer
    fun getStateIsFavorite(): LiveData<Boolean> = stateIsFavorite

    private fun renderState(state: TrackScreenState) {
        screenStateMediaPlayer.postValue(state)
    }

    fun preparePlayer() {
        if (mediaPlayerInteractor.getState() == PlayerState.CHANGED_CONFIG) {
            playbackControl()
        }
    }

    fun playbackControl() {
        mediaPlayerInteractor.playbackControl()
        when (mediaPlayerInteractor.getState()) {
            PlayerState.PLAYING -> startTimer()
            PlayerState.PAUSED -> pausePlayer()
            else -> {}
        }
    }

    private fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        renderState(
            TrackScreenState
                .Paused(getTime())
        )
    }

    fun onChangeConfig() {
        if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
            mediaPlayerInteractor.onChangeConfig()
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
                delay(TIME_UPDATE_DELAY)
                renderState(TrackScreenState.Playing(getTime()))
            }
            if (mediaPlayerInteractor.getState() == PlayerState.PREPARED) {
                renderState(TrackScreenState.Prepared(getTime()))
            }
        }
    }

    private fun getTime(): String {
        return millisecondToMinute(mediaPlayerInteractor.getCurrentPosition())
    }

    fun onClickFavorite() {
        viewModelScope.launch {
            if (track != null) {
                if (stateIsFavorite.value == true) {
                    favoritesTracksInteractor.deleteTrack(track!!)
                    track?.isFavorite = false
                    stateIsFavorite.postValue(false)
                } else {
                    favoritesTracksInteractor.addTrack(track!!)
                    track?.isFavorite = true
                    stateIsFavorite.postValue(true)
                }
            }
        }
    }

    companion object {
        private const val TIME_UPDATE_DELAY = 300L
    }
}
