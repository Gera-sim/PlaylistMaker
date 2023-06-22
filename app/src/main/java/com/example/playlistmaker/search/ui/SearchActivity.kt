package com.example.playlistmaker.search.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.TRACK
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.ui.models.SearchState

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private val searchAdapter = TracksAdapter {clickOnTrackItem(it)}
    private val historyAdapter = TracksAdapter {clickOnTrackItem(it)}
    enum class PlaceHolder { SEARCH_RES, NOT_FOUND, ERROR, HISTORY, PROGRESS_BAR }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        viewModel.observeState().observe(this) { render(it) }
        viewModel.observeShowToast().observe(this) { showToast(it) }


        binding.Home2.setOnClickListener { finish()}
        binding.rvSearchResults.adapter = searchAdapter
        binding.rvHistory.adapter = historyAdapter
        binding.clear.setOnClickListener { clearSearch() }

        binding.searchForm.doOnTextChanged { s: CharSequence?, _, _, _ ->
        binding.clear.visibility = clearButtonVisibility(s)
        if (binding.searchForm.hasFocus() && s.toString().isNotEmpty()) {showPlaceholder(PlaceHolder.SEARCH_RES)}
        viewModel.searchDebounce(binding.searchForm.text.toString())
    }

//    обработка нажатия поиска на клавиатуре
    binding.searchForm.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            viewModel.search(binding.searchForm.text.toString())
        }
        false }

//    обработка кнопки "Обновить"
    binding.errorButton.setOnClickListener {
        viewModel.search(binding.searchForm.text.toString()) }

//    обработка кнопки "Очистить историю"
    binding.clearHistoryButton.setOnClickListener { viewModel.clearTracksHistory() }

    binding.clear.visibility = clearButtonVisibility(binding.searchForm.text)

    binding.searchForm.requestFocus()
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.SearchResult -> {
                searchAdapter.tracks = state.tracks
                showPlaceholder(PlaceHolder.SEARCH_RES)}

            is SearchState.History -> {
                historyAdapter.tracks = state.tracks
                showPlaceholder(PlaceHolder.HISTORY)}

            is SearchState.Error -> {
                binding.errorText.text = state.message
                showPlaceholder(PlaceHolder.ERROR)}

            is SearchState.NotFound -> showPlaceholder(PlaceHolder.NOT_FOUND)

            is SearchState.Loading -> showPlaceholder(PlaceHolder.PROGRESS_BAR)
        }}

    private fun showPlaceholder(placeholder: PlaceHolder) {
        when (placeholder) {
            PlaceHolder.NOT_FOUND -> {
                binding.rvSearchResults.visibility = View.GONE
                binding.youSearched.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.VISIBLE
                binding.placeholderError.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }
            PlaceHolder.ERROR -> {
                binding.rvSearchResults.visibility = View.GONE
                binding.youSearched.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.GONE
                binding.placeholderError.visibility = View.VISIBLE
                binding.searchProgressBar.visibility = View.GONE
            }
            PlaceHolder.HISTORY -> {
                binding.rvSearchResults.visibility = View.GONE
                binding.youSearched.visibility = View.VISIBLE
                binding.placeholderNotFound.visibility = View.GONE
                binding.placeholderError.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }
            PlaceHolder.SEARCH_RES -> {
                binding.rvSearchResults.visibility = View.VISIBLE
                binding.youSearched.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.GONE
                binding.placeholderError.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }
            PlaceHolder.PROGRESS_BAR -> {
                binding.rvSearchResults.visibility = View.GONE
                binding.youSearched.visibility = View.GONE
                binding.placeholderNotFound.visibility = View.GONE
                binding.placeholderError.visibility = View.GONE
                binding.searchProgressBar.visibility = View.VISIBLE
            }}}

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }}

private fun clearSearch() {
    searchAdapter.tracks = arrayListOf()
    binding.searchForm.setText("")
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    viewModel.clearSearch()
}

    private fun showToast(additionalMessage: String) {
    Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()}

    private fun clickOnTrackItem(track: Track) {
        if (viewModel.clickDebounce()){
            viewModel.addTracksHistory(track)
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra(TRACK, track)
        }
        startActivity(intent)
    }}}