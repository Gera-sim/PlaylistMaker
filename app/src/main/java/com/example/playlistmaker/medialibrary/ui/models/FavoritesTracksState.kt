package com.example.playlistmaker.medialibrary.ui.models

import com.example.playlistmaker.search.domain.model.Track

sealed interface FavoritesTracksState {

    object Empty : FavoritesTracksState

    data class FavoritesTracks(
        val tracks: ArrayList<Track>
    ) : FavoritesTracksState
}