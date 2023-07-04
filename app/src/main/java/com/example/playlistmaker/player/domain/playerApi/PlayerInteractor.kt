package com.example.playlistmaker.player.domain.playerApi

interface PlayerInteractor {
    fun preparePlayer(url: String,
                      onPreparedListener: () -> Unit,
                      onCompletionListener: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
}