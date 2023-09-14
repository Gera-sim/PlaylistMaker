package com.example.playlistmaker.search.data.local

import com.example.playlistmaker.common.models.Track

interface LocalStorage {
    fun addTracksHistory(track: Track)
    fun clearTracksHistory()
    fun getTracksHistory(): List<Track>
}