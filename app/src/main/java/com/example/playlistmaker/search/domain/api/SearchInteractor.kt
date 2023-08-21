package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchTracks(expression: String): Flow<Pair<ArrayList<Track>?, Int?>>

    fun addTracksHistory(track: Track)
    fun clearTracksHistory()
    fun getTracksHistory(): ArrayList<Track>}
