package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.models.TrackScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.millisecondToMinute

class TrackViewModel(
    private var track: Track?,
    private var mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {


    companion object {
        private val MEDIA_PLAYER_TOKEN = Any()
        private const val TIME_UPDATE_DELAY = 500L
    }

    private val handler = Handler(Looper.getMainLooper())

    private val screenStateMediaPlayer = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)

    private val run = timerUpdater()

    fun getScreenStateMediaPlayer(): LiveData<TrackScreenState> = screenStateMediaPlayer

    private fun renderState(state: TrackScreenState) {
        screenStateMediaPlayer.postValue(state)
    }

    private fun preparePlayer() {
        mediaPlayerInteractor.preparePlayer()
        renderState(
            TrackScreenState.Prepared(
                millisecondToMinute(track?.trackTimeMillis ?: "")
            )
        )
    }

    fun playbackControl() {
        mediaPlayerInteractor.playbackControl()
        run.run()
    }

    fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    private fun timerUpdater(): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayerInteractor.getState() == PlayerState.PLAYING) {
                    renderState(
                        TrackScreenState.Play(
                            millisecondToMinute(mediaPlayerInteractor.getCurrentPosition())
                        )
                    )
                    handler.postDelayed(this, MEDIA_PLAYER_TOKEN, TIME_UPDATE_DELAY)
                } else if (mediaPlayerInteractor.getState() == PlayerState.PREPARED) {

                    handler.removeCallbacks(this, MEDIA_PLAYER_TOKEN)
                    renderState(
                        TrackScreenState
                            .Prepared(millisecondToMinute(mediaPlayerInteractor.getCurrentPosition()))
                    )
                } else if (mediaPlayerInteractor.getState() == PlayerState.PAUSED) {
                    handler.removeCallbacks(this, MEDIA_PLAYER_TOKEN)
                    renderState(
                        TrackScreenState
                            .Pause(millisecondToMinute(mediaPlayerInteractor.getCurrentPosition()))
                    )
                }
            }
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(MEDIA_PLAYER_TOKEN)
        releasePlayer()
    }

    init {
        preparePlayer()
    }
}
