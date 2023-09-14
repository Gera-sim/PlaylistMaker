package com.example.playlistmaker.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.search.ui.models.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    //1
    private val stateLiveData = MutableLiveData<SearchState>()
    private val showToast = SingleLiveEvent<String>()

    //2
    fun observeState(): LiveData<SearchState> = stateLiveData
    fun observeShowToast(): LiveData<String> = showToast

    private var isClickAllowed = true

    private var debounceJob: Job? = null

    init {
        val historyTracks = getTracksHistory()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchState.History(historyTracks))
        }
    }

//    private val tracksSearchDebounce =
//        debounce<String>(SEARCH_DEBOUNCE_DELAY_MILLIS, viewModelScope, true) { searchText ->
//            search(searchText)
//        }

    fun searchDebounce(searchText: String) {
        if (searchText.isNotEmpty()) {
            debounceJob?.cancel()
            debounceJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
                search(searchText)
            }
        }
    }

    fun search(searchText: String) {
        if (searchText.isNotEmpty()) {

            debounceJob?.cancel()

            renderState(SearchState.Loading)

            viewModelScope.launch {
                searchInteractor
                    .searchTracks(searchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    fun clearSearch() {
        val historyTracks = getTracksHistory()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchState.History(historyTracks))
        } else {
            renderState(SearchState.SearchResult(listOf()))
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorCode: Int?) {
        var tracks = listOf<Track>()
        if (foundTracks != null) {
            tracks = (foundTracks)
        }

        when {
            errorCode != null -> {
                renderState(SearchState.Error(errorState = errorCode))
            }

            tracks.isEmpty() -> {
                renderState(SearchState.NotFound)
            }

            else -> {
                renderState(SearchState.SearchResult(tracks = tracks))
            }
        }
    }

    fun addTracksHistory(track: Track) {
        searchInteractor.addTracksHistory(track)

        val updatedList = searchInteractor.getTracksHistory()
        stateLiveData.value = SearchState.History(updatedList)
    }
//        из SPR16: две ошибки:
//        после addTracksHistory() viewModel не отправляет изменённый
//        список через liveData, поэтому активити не получает обновление;

//        в обработчике onClick берётся неправильный индекс элемента -
//        надо брать int adapterPosition = holder.getAdapterPosition(),
//        иначе будет использоваться индекс, который был при вызове onBindViewHolder


    fun clearTracksHistory(text: String) {
        searchInteractor.clearTracksHistory()
        renderState(SearchState.SearchResult(listOf()))
        showToast.postValue(text)
    }

    private fun getTracksHistory(): List<Track> {
        return searchInteractor.getTracksHistory()
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}