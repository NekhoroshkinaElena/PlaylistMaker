package com.example.playlistmaker.data.history

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.TrackStorage
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "search_history_key"

class TrackHistoryStorage(private val sharedPreferences: SharedPreferences) : TrackStorage {
    override fun getSearchHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
            ?: return arrayListOf()
        return ArrayList(Gson().fromJson(json, Array<Track>::class.java).toList())
    }

    override fun saveSearchHistory(tracks: List<Track>) {

        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit().clear().apply()
    }
}
