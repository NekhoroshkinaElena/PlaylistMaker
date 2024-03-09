package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.TrackPlayerImpl
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.search.data.Requester
import com.example.playlistmaker.search.data.history.TrackHistoryStorage
import com.example.playlistmaker.search.data.network.RetrofitRequester
import com.example.playlistmaker.search.data.network.TrackApiService
import com.example.playlistmaker.search.domain.TrackStorage
import com.example.playlistmaker.search.ui.activity.SEARCH_HISTORY_PREFERENCES
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<TrackApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<Requester> {
        RetrofitRequester(get(), androidContext())
    }

    single<TrackStorage> {
        TrackHistoryStorage(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}
