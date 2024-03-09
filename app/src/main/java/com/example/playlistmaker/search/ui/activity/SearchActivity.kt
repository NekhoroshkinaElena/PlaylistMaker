package com.example.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.TrackActivity
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.TRACK_KEY
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.models.SearchScreenState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var binding: ActivitySearchBinding

    private lateinit var queryInput: EditText
    private lateinit var textWatcher: TextWatcher
    private lateinit var searchHistoryGroup: ViewGroup
    private lateinit var toolbar: Toolbar
    private lateinit var tracksList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var buttonClearHistory: Button
    private lateinit var clearSearchBar: ImageView
    private lateinit var tracksHistory: RecyclerView
    private lateinit var buttonUpdate: Button

    private lateinit var userInput: String

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }

    private val trackClickListener = object : TrackAdapter.TrackClickListener {
        override fun onTrackClick(track: Track) {
            if (clickDebounce()) {
                val intent = Intent(this@SearchActivity, TrackActivity::class.java)
                intent.putExtra(TRACK_KEY, track)
                startActivity(intent)

                viewModel.addTrackToHistory(track)
            }
        }
    }

    private val trackAdapter = TrackAdapter(
        trackClickListener
    )

    private val trackAdapterHistory = TrackAdapter(
        trackClickListener
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchHistoryGroup = binding.searchHistoryGroup
        toolbar = binding.toolbarSearch
        queryInput = binding.searchField
        tracksList = binding.rvTrackList
        progressBar = binding.progressBar
        placeholderImage = binding.placeholderImage
        placeholderMessage = binding.placeholderMessage
        buttonClearHistory = binding.buttonClearHistory
        clearSearchBar = binding.clearSearchBar
        tracksHistory = binding.rvHistoryList
        buttonUpdate = binding.buttonUpdate

        toolbar.setNavigationOnClickListener {
            finish()
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearSearchBar.visibility = clearButtonVisibility(s)
                if (s?.isEmpty() == true) {
                    viewModel.onFocused()
                } else {
                    viewModel.searchDebounce(s?.toString() ?: "")
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        queryInput.setOnFocusChangeListener { view, b ->
            if (b) {
                viewModel.onFocused()
            }
        }

        buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        buttonUpdate.setOnClickListener {
            viewModel.updateSearch()
        }

        textWatcher.let { queryInput.addTextChangedListener(it) }

        viewModel.getScreenStateLiveData().observe(this) {
            render(it)
        }

        clearSearchBar.setOnClickListener {
            queryInput.setText("")
        }

        tracksList.adapter = trackAdapter

        tracksHistory.adapter = trackAdapterHistory
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Unfocused -> showUnfocused()
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Content -> showContent(state.tracks)
            is SearchScreenState.Empty -> showEmpty()
            is SearchScreenState.Error -> showError()
            is SearchScreenState.History -> showHistory(state.tracks)
        }
    }

    private fun showUnfocused() {
        searchHistoryGroup.isVisible = false
        tracksList.isVisible = false
        progressBar.isVisible = false
        placeholderMessage.isVisible = false
        placeholderImage.isVisible = false
        buttonUpdate.isVisible = false
    }

    private fun showContent(tracks: List<Track>) {
        Log.i("TAG", "showContent: $tracks")
        tracksList.isVisible = true
        searchHistoryGroup.isVisible = false
        progressBar.isVisible = false
        placeholderMessage.isVisible = false
        placeholderImage.isVisible = false
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
        buttonUpdate.isVisible = false
    }

    private fun showLoading() {
        tracksList.isVisible = false
        searchHistoryGroup.isVisible = false
        progressBar.isVisible = true
        placeholderMessage.isVisible = false
        placeholderImage.isVisible = false
        buttonUpdate.isVisible = false
    }

    private fun showHistory(tracks: List<Track>) {
        tracksList.isVisible = false
        searchHistoryGroup.isVisible = true
        progressBar.isVisible = false
        placeholderMessage.isVisible = false
        placeholderImage.isVisible = false

        trackAdapterHistory.tracks.clear()
        trackAdapterHistory.tracks.addAll(tracks)
        trackAdapterHistory.notifyDataSetChanged()
        buttonUpdate.isVisible = false
    }

    private fun showError() {
        tracksList.isVisible = false
        searchHistoryGroup.isVisible = false
        progressBar.isVisible = false
        placeholderMessage.text = getString(R.string.something_went_wrong)
        placeholderImage.setImageDrawable(getDrawable(R.drawable.ic_something_went_wrong))
        placeholderMessage.isVisible = true
        placeholderImage.isVisible = true
        buttonUpdate.isVisible = true
    }

    private fun showEmpty() {
        tracksList.isVisible = false
        searchHistoryGroup.isVisible = false
        progressBar.isVisible = false
        placeholderMessage.text = getString(R.string.nothing_found)
        placeholderImage.setImageDrawable(getDrawable(R.drawable.ic_not_found))
        placeholderMessage.isVisible = true
        placeholderImage.isVisible = true
        buttonUpdate.isVisible = false
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
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
}
