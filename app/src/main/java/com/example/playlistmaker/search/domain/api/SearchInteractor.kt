package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int?>>

    fun addTracksHistory(track: Track)
    fun clearTracksHistory()
    fun getTracksHistory(): List<Track>}
