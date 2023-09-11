package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
    fun addTracksHistory(track: Track)
    fun clearTracksHistory()
    fun getTracksHistory(): List<Track>
}