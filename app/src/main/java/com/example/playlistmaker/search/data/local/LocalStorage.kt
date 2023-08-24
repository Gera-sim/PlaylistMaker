package com.example.playlistmaker.search.data.local

import com.example.playlistmaker.search.domain.model.Track

interface LocalStorage {
    fun addTracksHistory(track: Track)
    fun clearTracksHistory()
    fun getTracksHistory(): List<Track>
}