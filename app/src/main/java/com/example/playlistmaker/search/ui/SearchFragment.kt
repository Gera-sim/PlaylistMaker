package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.ui.TracksAdapter
import com.example.playlistmaker.search.ui.models.SearchState
import com.example.playlistmaker.common.utils.RESULT_CODE_EMPTY
import com.example.playlistmaker.common.utils.TRACK
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by viewModel<SearchViewModel>()
    private val searchAdapter = TracksAdapter({ clickOnTrackItem(it) })
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val historyAdapter = TracksAdapter({ clickOnTrackItem(it) })

    enum class PlaceHolder { SEARCH_RES, NOT_FOUND, ERROR, HISTORY, PROGRESS_BAR }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.observeState().observe(viewLifecycleOwner) { render(it) }

        searchViewModel.observeShowToast().observe(viewLifecycleOwner) { showToast(it) }

        binding.rvSearchResults.adapter = searchAdapter
        binding.rvHistory.adapter = historyAdapter

        binding.clear.setOnClickListener { clearSearch() }

        binding.searchForm.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.clear.visibility = clearButtonVisibility(s)
            if (
                binding.searchForm.hasFocus()
                && s.toString().isNotEmpty()
            ) {
                showPlaceholder(PlaceHolder.SEARCH_RES)
            }
            searchViewModel.searchDebounce(binding.searchForm.text.toString())
        }

//    обработка нажатия поиска на клавиатуре
        binding.searchForm.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.search(binding.searchForm.text.toString())
            }
            false
        }

//    обработка кнопки "Обновить"
        binding.errorButton.setOnClickListener {
            searchViewModel.search(binding.searchForm.text.toString())
        }

//    обработка кнопки "Очистить историю"
        binding.clearHistoryButton.setOnClickListener {
            confirmDialog.show()
        }

        binding.clear.visibility = clearButtonVisibility(binding.searchForm.text)

        binding.searchForm.requestFocus()


        confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.clear_history_question))
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            setPositiveButton(getString(R.string.clear)) { _, _ ->
                searchViewModel.clearTracksHistory(getString(R.string.history_was_deleted))
            }
        }

    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.SearchResult -> {
                searchAdapter.tracks = state.tracks
                showPlaceholder(PlaceHolder.SEARCH_RES)
            }

            is SearchState.History -> {
                historyAdapter.tracks = state.tracks
                showPlaceholder(PlaceHolder.HISTORY)
            }

            is SearchState.Error -> {
                when (state.errorState) {
                    RESULT_CODE_EMPTY -> binding.errorText.text =
                        resources.getText(R.string.check_internet_connection)

                    else -> binding.errorText.text = String.format(
                        resources.getText(R.string.error).toString(),
                        state.errorState
                    )
                }

                showPlaceholder(PlaceHolder.ERROR)
            }


            is SearchState.NotFound -> showPlaceholder(PlaceHolder.NOT_FOUND)

            is SearchState.Loading -> showPlaceholder(PlaceHolder.PROGRESS_BAR)
        }
    }

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
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearSearch() {
        searchAdapter.tracks = arrayListOf()
        binding.searchForm.setText("")
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        searchViewModel.clearSearch()
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_SHORT).show()
    }

    private fun clickOnTrackItem(track: Track) {
        if (searchViewModel.clickDebounce()) {
            searchViewModel.addTracksHistory(track)
            findNavController().navigate(
                R.id.action_to_PlayerFragment,
                Bundle().apply {
                    putSerializable(TRACK, track)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


}}