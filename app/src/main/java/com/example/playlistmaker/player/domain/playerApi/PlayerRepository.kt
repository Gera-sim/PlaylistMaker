package com.example.playlistmaker.player.domain.playerApi

interface PlayerRepository {
    fun preparePlayer(url: String,
                      onPreparedListener: () -> Unit,
                      onCompletionListener: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
}