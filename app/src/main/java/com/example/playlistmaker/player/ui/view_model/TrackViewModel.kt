package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private var mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    companion object {
        private const val TIME_UPDATE_DELAY = 300L
    }

    private val screenStateMediaPlayer =
        MutableLiveData<TrackScreenState>(TrackScreenState.Default())

    private var timerJob: Job? = null

    init {
        mediaPlayerInteractor.preparePlayer()
        renderState(
            TrackScreenState.Prepared(
                millisecondToMinute(track?.trackTimeMillis ?: "")
            )
        )
    }

    fun getScreenStateMediaPlayer(): LiveData<TrackScreenState> = screenStateMediaPlayer

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
}
