package com.example.playlistmaker.domain.api

interface OnPlayerPreparedListener {
    fun playerOnPrepared()
    fun playerOnCompletion()
}