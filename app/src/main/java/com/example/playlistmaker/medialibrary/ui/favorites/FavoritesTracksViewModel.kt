package com.example.playlistmaker.medialibrary.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.medialibrary.ui.models.FavoritesTracksState

class FavoritesTracksViewModel : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesTracksState>()
    fun observeState(): LiveData<FavoritesTracksState> = stateLiveData

    init {
        renderState(FavoritesTracksState.Empty)
    }

    private fun renderState(state: FavoritesTracksState) {
        stateLiveData.postValue(state)
    }
}