package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.Resource

interface SearchRepository {
    fun searchTracks(expression: String): Resource<ArrayList<Track>>
    fun addTracksHistory(track: Track)
    fun clearTracksHistory()
    fun getTracksHistory(): ArrayList<Track>
}