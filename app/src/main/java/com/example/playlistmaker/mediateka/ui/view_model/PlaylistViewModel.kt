package com.example.playlistmaker.mediateka.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.domain.PlaylistsInteractor
import com.example.playlistmaker.mediateka.domain.model.Playlist
import com.example.playlistmaker.mediateka.ui.models.PlaylistState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.util.changeTheEndingOfTheWord
import com.example.playlistmaker.util.changeTheEndingOfTheWordMinute
import com.example.playlistmaker.util.millisecondToMinute
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Int?,
    private val playlistsInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val statePlaylistLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = statePlaylistLiveData

    private var listTrack: List<Track> = mutableListOf()

    private var playlist: Playlist? = null

    init {
        viewModelScope.launch {
            fullData()
        }
    }

    private fun fullData() {
        viewModelScope.launch {
            if (playlistId != null) {
                playlist = playlistsInteractor.getPlaylistById(playlistId)

                val list: MutableList<Track> = mutableListOf()
                playlist?.tracksIds?.map {
                    list.add(playlistsInteractor.getTrackById(it))
                }

                list.reverse()
                listTrack = list
                renderState()
            }
        }
    }

    fun updatePlaylist() {
        fullData()
    }

    private fun renderState() {
        if (playlist != null) {
            statePlaylistLiveData.postValue(
                PlaylistState.Content(
                    playlist?.urlImage,
                    playlist!!.playlistName,
                    playlist?.description,
                    getDurationAllTrack(listTrack),
                    listTrack.size.toString() + " " +
                            changeTheEndingOfTheWord(listTrack.size),
                    listTrack
                )
            )
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            if (playlistId != null) {
                playlistsInteractor.deletePlaylist(playlistId)
                statePlaylistLiveData.postValue(PlaylistState.Delete)
            }
        }
    }

    private fun getDurationAllTrack(tracksInPlaylist: List<Track>?): String {
        var duration = 0
        viewModelScope.launch {
            tracksInPlaylist?.map {
                duration += it.trackTimeMillis.toInt()
            }
        }
        return millisecondToMinute(duration.toString()) + " " + changeTheEndingOfTheWordMinute(
            duration
        )
    }

    fun deleteTrackFromPlaylist(trackId: Int) {
        viewModelScope.launch {
            val playlist = playlistsInteractor.getPlaylistById(playlistId!!)
            playlistsInteractor.updatePlaylistDeleteTrack(
                trackId,
                playlist
            )
            fullData()
        }
    }

    fun sharePlaylist() {
        viewModelScope.launch {
            if (playlistId != null) {
                val list: StringBuilder = StringBuilder()
                listTrack.mapIndexed { index: Int, track: Track ->
                    list.append("${(index + 1)}. $track")
                }
                val messages = "${playlist?.playlistName}\n" + "${playlist?.description}\n" +
                        "${playlist?.tracksCount}\n" + list

                sharingInteractor.sharePlaylist(
                    messages
                )
            }
        }
    }
}
