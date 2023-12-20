package com.example.playlistmaker

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var toolbar: Toolbar
    private lateinit var searchField: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerViewTrack: RecyclerView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var buttonUpdate: Button
    private lateinit var searchHistoryGroup: LinearLayout
    private lateinit var searchHistoryList: RecyclerView
    private lateinit var buttonClearSearchHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.toolbar_search)
        searchField = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        recyclerViewTrack = findViewById(R.id.trackList)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        searchHistoryGroup = findViewById(R.id.searchHistoryGroup)
        searchHistoryList = findViewById(R.id.historyList)
        buttonClearSearchHistory = findViewById(R.id.buttonClearHistory)

        val sharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)

        val searchHistory = SearchHistory(sharedPreferences)

        trackAdapter.setSearchHistory(searchHistory)
        searchHistoryTrackAdapter.setSearchHistory(searchHistory)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        buttonClearSearchHistory.setOnClickListener {
            searchHistory.clearTheHistory()
            searchHistoryTrack.clear()
            searchHistoryTrackAdapter.notifyDataSetChanged()
            searchHistoryGroup.visibility = View.GONE
        }

        searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchField.text.isEmpty() && searchHistory.getSearchHistory()
                    .isNotEmpty()
            ) {
                searchHistoryTrack.clear()
                searchHistoryTrack.addAll(searchHistory.getSearchHistory())
                searchHistoryTrackAdapter.notifyDataSetChanged()

                searchHistoryGroup.visibility = View.VISIBLE

            } else {
                searchHistoryGroup.visibility = View.GONE
            }
        }

        searchField.setText(VALUE_USER_INPUT)

        clearButton.setOnClickListener {
            searchHistoryTrack.clear()
            searchHistoryTrack.addAll(searchHistory.getSearchHistory())
            searchHistoryTrackAdapter.notifyDataSetChanged()
            trackAdapter.notifyDataSetChanged()
            searchField.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
            listTracks.clear()
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            buttonUpdate.visibility = View.GONE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                userInput = s.toString()
                searchHistoryGroup.visibility =
                    if (searchField.hasFocus() && s?.isEmpty() == true && searchHistory.getSearchHistory()
                            .isNotEmpty()
                    ) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        searchField.addTextChangedListener(textWatcher)

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        trackAdapter.results = listTracks

        recyclerViewTrack.adapter = trackAdapter

        searchHistoryTrackAdapter.results = searchHistoryTrack

        searchHistoryList.adapter = searchHistoryTrackAdapter
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
            placeholderMessage.visibility = View.GONE
            buttonUpdate.visibility = View.GONE
            placeholderImage.visibility = View.GONE

        } else {
            placeholderMessage.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
        }
        when (text) {
            getString(R.string.something_went_wrong) -> {
                buttonUpdate.visibility = View.VISIBLE
            }

            else -> buttonUpdate.visibility = View.GONE
        }
        listTracks.clear()
        trackAdapter.notifyDataSetChanged()
        placeholderMessage.text = text
        placeholderImage.setImageDrawable(image)
        buttonUpdate.setOnClickListener {
            search()
        }
    }
}
