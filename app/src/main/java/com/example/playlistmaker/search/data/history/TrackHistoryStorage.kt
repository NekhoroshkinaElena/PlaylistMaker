package com.example.playlistmaker.search.data.history

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.TrackStorage
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson

private const val SEARCH_HISTORY_KEY = "search_history_key"

class TrackHistoryStorage(
    private val historyPrefs: SharedPreferences,
    private val gson: Gson
) : TrackStorage {
    override fun getSearchHistory(): ArrayList<Track> {
        val json = historyPrefs.getString(SEARCH_HISTORY_KEY, null)
            ?: return arrayListOf()
        return ArrayList(gson.fromJson(json, Array<Track>::class.java).toList())
    }

    override fun saveSearchHistory(tracks: List<Track>) {
        val json = gson.toJson(tracks)
        historyPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    override fun clearHistory() {
        historyPrefs.edit().clear().apply()
    }
}
