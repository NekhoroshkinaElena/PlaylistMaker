package com.example.playlistmaker

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.history.SearchHistory
import com.example.playlistmaker.track.Track
import com.example.playlistmaker.track.TrackAdapter
import com.example.playlistmaker.track.TrackApi
import com.example.playlistmaker.track.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"

class SearchActivity : AppCompatActivity() {

    private var userInput: String = VALUE_USER_INPUT

    companion object {
        const val USER_INPUT = "USER_INPUT"
        const val VALUE_USER_INPUT = ""
    }

    private val trackBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create<TrackApi>()

    private val listTracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(listTracks)

    private val searchHistoryTrack = ArrayList<Track>()
    private val searchHistoryTrackAdapter = TrackAdapter(searchHistoryTrack)

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userInput = savedInstanceState.getString(USER_INPUT, VALUE_USER_INPUT)
    }

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)

        val searchHistory = SearchHistory(sharedPreferences)

        trackAdapter.setSearchHistory(searchHistory)
        searchHistoryTrackAdapter.setSearchHistory(searchHistory)

        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }

        binding.buttonClearHistory.setOnClickListener {
            searchHistory.clearTheHistory()
            searchHistoryTrack.clear()
            searchHistoryTrackAdapter.notifyDataSetChanged()
            binding.searchHistoryGroup.visibility = View.GONE
        }

        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchField.text.isEmpty() && searchHistory.getSearchHistory()
                    .isNotEmpty()
            ) {
                searchHistoryTrack.clear()
                searchHistoryTrack.addAll(searchHistory.getSearchHistory())
                searchHistoryTrackAdapter.notifyDataSetChanged()

                binding.searchHistoryGroup.visibility = View.VISIBLE

            } else {
                binding.searchHistoryGroup.visibility = View.GONE
            }
        }

        binding.searchField.setText(VALUE_USER_INPUT)

        binding.clearSearchBar.setOnClickListener {
            searchHistoryTrack.clear()
            searchHistoryTrack.addAll(searchHistory.getSearchHistory())
            searchHistoryTrackAdapter.notifyDataSetChanged()
            trackAdapter.notifyDataSetChanged()
            binding.searchField.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchField.windowToken, 0)
            listTracks.clear()
            binding.placeholderMessage.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE
            binding.buttonUpdate.visibility = View.GONE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBar.visibility = clearButtonVisibility(s)
                userInput = s.toString()
                binding.searchHistoryGroup.visibility =
                    if (binding.searchField.hasFocus() && s?.isEmpty() == true && searchHistory.getSearchHistory()
                            .isNotEmpty()
                    ) View.VISIBLE else View.GONE
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
        trackService.search(userInput)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                showResponse("", null)
                                listTracks.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            } else {
                                showResponse(
                                    getString(R.string.nothing_found),
                                    getDrawable(R.drawable.ic_not_found)
                                )
                            }
                        }

                        404 -> showResponse(
                            getString(R.string.nothing_found),
                            getDrawable(R.drawable.ic_not_found)
                        )

                        else -> {
                            showResponse(
                                getString(R.string.something_went_wrong),
                                getDrawable(R.drawable.ic_something_went_wrong)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showResponse(
                        getString(R.string.something_went_wrong),
                        getDrawable(R.drawable.ic_something_went_wrong)
                    )
                }
            })
    }

    private fun showResponse(text: String, image: Drawable?) {
        if (text.isEmpty()) {
            binding.placeholderMessage.visibility = View.GONE
            binding.buttonUpdate.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE

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
            search()
        }
    }
}
