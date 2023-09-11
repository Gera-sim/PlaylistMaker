package com.example.playlistmaker.medialibrary.ui.models

import com.example.playlistmaker.common.models.PlayList

sealed interface PlayListsState {

    object Empty : PlayListsState

    data class PlayLists(
        val tracks: List<PlayList>
    ) : PlayListsState
}