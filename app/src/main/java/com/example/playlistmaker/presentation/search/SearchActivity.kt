package com.example.playlistmaker.presentation.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.*
import com.example.playlistmaker.data.ItunesAPI
import com.example.playlistmaker.data.SearchResponse
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.player.PlayerActivity
import com.google.gson.Gson
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    enum class PlaceHolder { SEARCH_RES, NOT_FOUND, ERROR, HISTORY, SEARCH_PROGRESS_BAR }

    private var searchInputQuery = ""

    private lateinit var searchInput: EditText
    private lateinit var clearInputButton: ImageView

    private lateinit var rvSearch: RecyclerView

    private lateinit var rvHistory: RecyclerView
    private lateinit var searchHistorySharedPref: SearchHistorySharedPref

    private lateinit var placeholderNotFound: TextView
    private lateinit var placeholderError: LinearLayout

    private lateinit var searched: LinearLayout

    private lateinit var searchProgressBar: ProgressBar

    private val baseUrl = "http://itunes.apple.com"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(ItunesAPI::class.java)

    private val searchAdapter = SearchAdapter {clickOnTrackItem(it) }

    private val historyAdapter = SearchAdapter {clickOnTrackItem(it)}

    private val searchRunnable = Runnable { search() }

    private val handler = Handler(Looper.getMainLooper())

    private var isClicked = true

    private val searchInputTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearInputButton.visibility = clearButtonVisibility(s)
            searchInputQuery = s.toString()
            if (searchInput.hasFocus() && searchInputQuery.isNotEmpty()) {
                showPlaceholder(PlaceHolder.SEARCH_RES)
            }
            searchDebounce()
                }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<ImageView>(R.id.Home2).setOnClickListener { finish() }
        findViewById<Button>(R.id.errorButton).setOnClickListener { search() }
        findViewById<ImageView>(R.id.clear).setOnClickListener { clearSearchForm()  }
        findViewById<Button>(R.id.clearHistoryButton).setOnClickListener { clearHistory() }

        findView()

        searchInput.addTextChangedListener(searchInputTextWatcher)
        searchInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchInput.text.isEmpty()) {
                showPlaceholder(PlaceHolder.SEARCH_RES)
            }
        }
        clearInputButton.visibility = clearButtonVisibility(searchInputQuery)

        searchInput.requestFocus()

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
        rvSearch.adapter = searchAdapter

        rvHistory.adapter = historyAdapter
        searchHistorySharedPref =
            SearchHistorySharedPref(getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE))
        if (searchInput.text.isEmpty()) {
            historyAdapter.tracks = searchHistorySharedPref.get()
            if (historyAdapter.tracks.isNotEmpty()) {
                showPlaceholder(PlaceHolder.HISTORY)
            }
        }
    }

    private fun search() {
        if (searchInputQuery.isNotEmpty()) {


            showPlaceholder(PlaceHolder.SEARCH_PROGRESS_BAR)
            api.search(searchInputQuery).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                searchAdapter.tracks = response.body()?.results!!
                                showPlaceholder(PlaceHolder.SEARCH_RES)
                            } else {
                                showPlaceholder(PlaceHolder.NOT_FOUND)
                            }
                        }
                        else -> {
                            showPlaceholder(PlaceHolder.ERROR)
                        }
                    }
                }
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showPlaceholder(PlaceHolder.ERROR)
                }
            })
        }
    }

    private fun showPlaceholder(placeholder: PlaceHolder) {
        when (placeholder) {
            PlaceHolder.NOT_FOUND -> {
                rvSearch.visibility = View.GONE
                searched.visibility = View.GONE
                placeholderNotFound.visibility = View.VISIBLE
                placeholderError.visibility = View.GONE
                searchProgressBar.visibility = View.GONE
            }
            PlaceHolder.ERROR -> {
                rvSearch.visibility = View.GONE
                searched.visibility = View.GONE
                placeholderNotFound.visibility = View.GONE
                placeholderError.visibility = View.VISIBLE
                searchProgressBar.visibility = View.GONE
            }
            PlaceHolder.HISTORY -> {
                rvSearch.visibility = View.GONE
                searched.visibility = View.VISIBLE
                placeholderNotFound.visibility = View.GONE
                placeholderError.visibility = View.GONE
                searchProgressBar.visibility = View.GONE
            }
            PlaceHolder.SEARCH_RES -> {
                rvSearch.visibility = View.VISIBLE
                searched.visibility = View.GONE
                placeholderNotFound.visibility = View.GONE
                placeholderError.visibility = View.GONE
                searchProgressBar.visibility = View.GONE
            }
            PlaceHolder.SEARCH_PROGRESS_BAR -> {
                rvSearch.visibility = View.GONE
                searched.visibility = View.GONE
                placeholderNotFound.visibility = View.GONE
                placeholderError.visibility = View.GONE
                searchProgressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun findView() {
        clearInputButton = findViewById(R.id.clear)
        searchInput = findViewById(R.id.SearchForm)
        searched = findViewById(R.id.youSearched)
        rvSearch = findViewById(R.id.rvSearchResults)
        rvHistory = findViewById(R.id.rvHistory)
        placeholderNotFound = findViewById(R.id.placeholderNotFound)
        placeholderError = findViewById(R.id.placeholderError)
        searchProgressBar = findViewById(R.id.searchProgressBar)
            }

    private fun clearHistory() {
        searchHistorySharedPref.clear()
        showPlaceholder(PlaceHolder.SEARCH_RES)
        Toast.makeText(
            applicationContext,
            getString(R.string.history_was_deleted),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchInputQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputQuery = savedInstanceState.getString(SEARCH_QUERY, "")
        if (searchInputQuery.isNotEmpty()) {
            searchInput.setText(searchInputQuery)
            search()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearSearchForm() {
        searchInput.setText("")
        historyAdapter.tracks = searchHistorySharedPref.get()
        if (historyAdapter.tracks.isNotEmpty()) {
            showPlaceholder(PlaceHolder.HISTORY)
        } else {
            showPlaceholder(PlaceHolder.SEARCH_RES)
        }
        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun clickOnTrackItem(track: Track) {
        if (clickDebounce()){
        searchHistorySharedPref.add(track)
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(TRACK, Gson().toJson(track))
        }
        startActivity(intent)
    }}

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
    private fun clickDebounce(): Boolean {
        val current = isClicked
        if (isClicked) {
            isClicked = false
            handler.postDelayed({ isClicked = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}