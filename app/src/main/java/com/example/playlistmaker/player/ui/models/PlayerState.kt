package com.example.playlistmaker.player.ui.models

sealed interface PlayerState {
    object Preparing : PlayerState
    object Playing : PlayerState
    object Paused : PlayerState
    object Stopped : PlayerState

    object Wait : PlayerState
    data class UpdatePlayingTime(val playingTime: String) : PlayerState
    data class StateFavorite(val isFavorite: Boolean) : PlayerState
}