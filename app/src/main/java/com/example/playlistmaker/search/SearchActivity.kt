package com.example.playlistmaker.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private var searchInputQuery = ""
    private lateinit var searchInput: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rvSearch: RecyclerView

    private val searchInputTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButton.visibility = clearButtonVisibility(s)
            searchInputQuery = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<ImageView>(R.id.Home2).setOnClickListener { finish() }
        searchInput = findViewById(R.id.SearchForm)
        searchInput.requestFocus()
        searchInput.addTextChangedListener(searchInputTextWatcher)

        clearButton = findViewById(R.id.clear)
        clearButton.visibility = clearButtonVisibility(searchInput.text)
        clearButton.setOnClickListener { clearSearchForm() }

        rvSearch = findViewById(R.id.rvSearchResults)
        rvSearch.adapter = SearchAdapter(mockTracks)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchInputQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInputQuery = savedInstanceState.getString(SEARCH_QUERY, "")
        searchInput.setText(searchInputQuery)
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

        val view = this.currentFocus
        if (view != null) {
            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private val mockTracks = arrayListOf(
        Track(
            "Smells Like Teen Spirit",
            "Nirvana",
            "5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),
        Track(
            "Billie Jean",
            "Michael Jackson",
            "4:35",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
        ),
        Track(
            "Stayin' Alive",
            "Bee Gees",
            "4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
        ),
        Track(
            "Whole Lotta Love",
            "Led Zeppelin",
            "5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
        ),
        Track(
            "Sweet Child O'Mine",
            "Guns N' Roses",
            "5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg",
        )
    )
}