package com.example.playlistmaker.search.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.model.Track

class TracksSearchViewModel(application: Application) : AndroidViewModel(application){

    private val stateLiveData = MutableLiveData<List<Track>>()
    fun getLoadingLiveData(): LiveData<List<Track>> = stateLiveData
    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            TracksSearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    as Application)
        }
    }}


}


