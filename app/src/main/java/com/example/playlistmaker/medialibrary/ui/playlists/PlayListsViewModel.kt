package com.example.playlistmaker.medialibrary.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.db.PlayListsInteractor
import com.example.playlistmaker.medialibrary.ui.models.PlayListsState
import kotlinx.coroutines.launch

class PlayListsViewModel(private val playListsInteractor: PlayListsInteractor): ViewModel() {
    private val stateLiveData = MutableLiveData<PlayListsState>()
    fun observeState(): LiveData<PlayListsState> = stateLiveData

    private fun renderState(state: PlayListsState) {
        stateLiveData.postValue(state)
    }

    fun requestPlayLists() {
        viewModelScope.launch {
            val playLists = playListsInteractor.getPlayLists()
            if (playLists.isEmpty()) {
                renderState(PlayListsState.Empty)
            } else {
                renderState(PlayListsState.PlayLists(playLists))
            }
        }
    }
}