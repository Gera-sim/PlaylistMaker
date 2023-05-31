package com.example.playlistmaker.domain

import com.example.playlistmaker.data.PlayerImpl
import com.example.playlistmaker.domain.api.OnPlayerPreparedListener

class PlayerInteractor(): Player {

    private val player = PlayerImpl()

    override fun preparePlayer(url: String, listener: OnPlayerPreparedListener) {
        player.preparePlayer(url, listener)
    }

    override fun startPlayer() {
        player.startPlayer()
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun releasePlayer() {
        player.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return player.getCurrentPosition()
    }

}