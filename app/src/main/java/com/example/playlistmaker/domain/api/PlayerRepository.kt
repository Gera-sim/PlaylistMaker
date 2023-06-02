package com.example.playlistmaker.domain.api

interface PlayerRepository {
    fun preparePlayer(url: String, listener: OnPlayerPreparedListener)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}