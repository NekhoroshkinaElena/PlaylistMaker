package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.mediateka.data.db.FavoritesTracksDatabase
import com.example.playlistmaker.mediateka.data.db.PlaylistsDatabase
import com.example.playlistmaker.mediateka.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.mediateka.data.db.converters.TrackDbConverter
import com.example.playlistmaker.player.data.TrackPlayerImpl
import com.example.playlistmaker.player.domain.TrackPlayer
import com.example.playlistmaker.search.data.Requester
import com.example.playlistmaker.search.data.network.RetrofitRequester
import com.example.playlistmaker.search.data.network.TrackApiService
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"

val dataModule = module {

    single<TrackApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApiService::class.java)
    }

    single(named(SEARCH_HISTORY_PREFERENCES)) {
        androidContext()
            .getSharedPreferences(SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    }

    single(named(PLAYLIST_MAKER_PREFERENCES)) {
        androidContext()
            .getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<Requester> {
        RetrofitRequester(trackApiService = get(), context = androidContext())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
    }

    factory<TrackPlayer> {
        TrackPlayerImpl(track = get(), mediaPlayer = get())
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            FavoritesTracksDatabase::class.java,
            "database.db"
        ).build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            PlaylistsDatabase::class.java,
            "databasePlaylists.db"
        ).build()
    }

    factory { TrackDbConverter() }

    factory { PlaylistDbConverter(gson = get()) }

    factory { Gson() }

    factory { MediaPlayer() }
}
