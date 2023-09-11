package com.example.playlistmaker.medialibrary.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.models.PlayList
import com.example.playlistmaker.medialibrary.domain.db.PlayListsInteractor
import kotlinx.coroutines.launch

class AddPlayListViewModel(private val playListsInteractor: PlayListsInteractor) : ViewModel() {

    fun createPlayList(name: String, description: String, image: String?) {
        val playList = PlayList(
            playListId = 0,
            name = name,
            description = description,
            image = image,
            tracksCount = 0
        )
        viewModelScope.launch {
            playListsInteractor.addPlayList(playList)
        }
    }



}