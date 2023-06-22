package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface SearchInteractor {

    fun searchTracks(expression: String, consumer: SearchConsumer)
    interface SearchConsumer {
        fun consume(foundTracks: ArrayList<Track>?, errorMessage: String?)
    }
    fun addTracksHistory(track: Track)
    fun clearTracksHistory()
    fun getTracksHistory(): ArrayList<Track>}
