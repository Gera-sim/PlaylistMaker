package com.example.playlistmaker.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    enum class PlaceHolder { SEARCH_RES, NOT_FOUND, ERROR }

    private var searchInputQuery = ""

    private lateinit var searchInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rvSearch: RecyclerView
    private lateinit var placeholderNotFound: TextView
    private lateinit var placeholderError: LinearLayout
    private lateinit var errorButton: Button


    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(ItunesAPI::class.java)
    private val adapter = SearchAdapter { showInfo(it) }

    private val searchInputTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButton.visibility = clearButtonVisibility(s)
            searchInputQuery = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<ImageView>(R.id.Home2).setOnClickListener { finish() }
        searchInput = findViewById(R.id.SearchForm)
        searchInput.requestFocus()
        searchInput.addTextChangedListener(searchInputTextWatcher)
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
        errorButton = findViewById(R.id.errorButton)
        errorButton.setOnClickListener { search() }

        clearButton = findViewById(R.id.clear)
        clearButton.visibility = clearButtonVisibility(searchInput.text)
        clearButton.setOnClickListener { clearSearchForm() }

        rvSearch = findViewById(R.id.rvSearchResults)
        rvSearch.adapter = adapter


        placeholderNotFound = findViewById(R.id.placeholderNotFound)
        placeholderError = findViewById(R.id.placeholderError)

    }

    private fun search() {
        if (searchInputQuery.isNotEmpty()) {
            api.search(searchInputQuery).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                adapter.tracks = response.body()?.results!!
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
                adapter.clearTracks()
                placeholderError.visibility = View.GONE
                placeholderNotFound.visibility = View.VISIBLE
            }
            PlaceHolder.ERROR -> {
                adapter.clearTracks()
                placeholderNotFound.visibility = View.GONE
                placeholderError.visibility = View.VISIBLE
            }
            else -> {
                placeholderNotFound.visibility = View.GONE
                placeholderError.visibility = View.GONE
            }
        }
    }

    private fun showInfo(track: Track) {
        val message = "${track.trackName}"
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
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
        adapter.clearTracks()
        showPlaceholder(PlaceHolder.SEARCH_RES)

        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}