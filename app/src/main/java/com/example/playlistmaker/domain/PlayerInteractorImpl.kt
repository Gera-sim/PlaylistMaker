package com.example.playlistmaker.domain

import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.OnPlayerPreparedListener

class PlayerInteractorImpl(): PlayerInteractor {

    private val player = PlayerRepositoryImpl()

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