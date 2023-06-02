package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.OnPlayerPreparedListener

interface PlayerInteractor {
    fun preparePlayer(url: String, listener: OnPlayerPreparedListener)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}