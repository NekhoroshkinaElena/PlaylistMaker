package com.example.playlistmaker.presentation.search

import com.example.playlistmaker.Creator
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmaker.R
import com.example.playlistmaker.data.history.SEARCH_HISTORY_KEY
import com.example.playlistmaker.data.history.TrackHistoryStorage
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksListResult

const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"

class SearchActivity : AppCompatActivity() {

    companion object {
        const val USER_INPUT = "USER_INPUT"
        const val VALUE_USER_INPUT = ""
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private var userInput: String = VALUE_USER_INPUT

    private val listTracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(listTracks) { clickDebounce() }

    private val searchHistoryTrack = ArrayList<Track>()
    private val searchHistoryTrackAdapter = TrackAdapter(searchHistoryTrack) { clickDebounce() }

    private val searchRunnable = Runnable { search() }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userInput = savedInstanceState.getString(USER_INPUT, VALUE_USER_INPUT)
    }


    private lateinit var tracksInteractor: TrackInteractor

    private lateinit var listener: OnSharedPreferenceChangeListener

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)

        tracksInteractor = Creator.provideTracksInteractor(TrackHistoryStorage(sharedPreferences))

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackAdapter.setSearchHistory(tracksInteractor)

        searchHistoryTrackAdapter.setSearchHistory(tracksInteractor)

        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }

        listener = OnSharedPreferenceChangeListener { _: SharedPreferences, key: String? ->
            if (key == SEARCH_HISTORY_KEY) {
                searchHistoryTrackAdapter.results = tracksInteractor.getSearchHistory()
                searchHistoryTrackAdapter.notifyDataSetChanged()
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        binding.buttonClearHistory.setOnClickListener {
            tracksInteractor.clearTheHistory()
            searchHistoryTrack.clear()
            searchHistoryTrackAdapter.notifyDataSetChanged()
            binding.searchHistoryGroup.visibility = View.GONE
        }

        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchField.text.isEmpty() && tracksInteractor.getSearchHistory()
                    .isNotEmpty()
            ) {
                searchHistoryTrack.clear()
                searchHistoryTrack.addAll(tracksInteractor.getSearchHistory())
                searchHistoryTrackAdapter.notifyDataSetChanged()

                binding.searchHistoryGroup.visibility = View.VISIBLE

            } else {
                binding.searchHistoryGroup.visibility = View.GONE
            }
        }

        binding.searchField.setText(VALUE_USER_INPUT)

        binding.clearSearchBar.setOnClickListener {
            searchHistoryTrack.clear()
            searchHistoryTrack.addAll(tracksInteractor.getSearchHistory())
            searchHistoryTrackAdapter.notifyDataSetChanged()
            trackAdapter.notifyDataSetChanged()
            binding.searchField.setText("")
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchField.windowToken, 0)
            listTracks.clear()
            hideFailedRequestMessage()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBar.visibility = clearButtonVisibility(s)
                userInput = s.toString()
                binding.searchHistoryGroup.visibility =
                    if (binding.searchField.hasFocus() && s?.isEmpty() == true &&
                        tracksInteractor.getSearchHistory()
                            .isNotEmpty()
                    ) {
                        listTracks.clear()
                        trackAdapter.notifyDataSetChanged()
                        View.VISIBLE
                    } else {
                        hideFailedRequestMessage()
                        View.GONE
                    }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.searchField.addTextChangedListener(textWatcher)

        binding.searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        binding.rvTrackList.adapter = trackAdapter

        binding.rvHistoryList.adapter = searchHistoryTrackAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_INPUT, userInput)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search() {
        if (userInput.isEmpty()) return
        listTracks.clear()
        trackAdapter.notifyDataSetChanged()
        binding.progressBar.isVisible = true
        tracksInteractor.searchTrack(userInput, consumer)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showResponse(text: String, image: Drawable?) {
        if (text.isEmpty()) {
            hideFailedRequestMessage()

        } else {
            binding.placeholderMessage.visibility = View.VISIBLE
            binding.placeholderImage.visibility = View.VISIBLE
        }
        when (text) {
            getString(R.string.something_went_wrong) -> {
                binding.buttonUpdate.visibility = View.VISIBLE
            }

            else -> binding.buttonUpdate.visibility = View.GONE
        }
        listTracks.clear()
        trackAdapter.notifyDataSetChanged()
        binding.placeholderMessage.text = text
        binding.placeholderImage.setImageDrawable(image)
        binding.buttonUpdate.setOnClickListener {
            hideFailedRequestMessage()
            search()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun hideFailedRequestMessage() {
        binding.placeholderMessage.visibility = View.GONE
        binding.buttonUpdate.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
    }

    private val consumer = object : TrackInteractor.TracksConsumer {
        override fun consume(tracksListResult: TracksListResult) {
            handler.post {
                binding.progressBar.isVisible = false
                if (tracksListResult.codeResponse == 200) {
                    if (tracksListResult.tracks.isEmpty()) {
                        showResponse(
                            getString(R.string.nothing_found),
                            getDrawable(R.drawable.ic_not_found)
                        )
                    } else {
                        showResponse("", null)
                        listTracks.addAll(tracksListResult.tracks)
                        trackAdapter.notifyDataSetChanged()
                    }
                } else if (tracksListResult.codeResponse == 404) {
                    showResponse(
                        getString(R.string.nothing_found),
                        getDrawable(R.drawable.ic_not_found)
                    )
                } else {
                    showResponse(
                        getString(R.string.something_went_wrong),
                        getDrawable(R.drawable.ic_something_went_wrong)
                    )
                }
            }
        }
    }
}
