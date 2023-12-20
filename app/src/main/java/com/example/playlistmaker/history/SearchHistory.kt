package com.example.playlistmaker.history

import android.content.SharedPreferences
import com.example.playlistmaker.track.Track
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "search_history_key"

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun getSearchHistory(): Array<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
            ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun addTrackToHistory(track: Track) {
        val tracks: MutableList<Track> = getSearchHistory().toMutableList()
        if (tracks.size >= 10) {
            tracks.removeAt(0)
        }
        if (tracks.contains(track)) {
            tracks.remove(track)
        }

        tracks.add(track)
        val json = Gson().toJson(tracks)

        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun clearTheHistory() {
        sharedPreferences.edit().clear().apply()
    }
}
