package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.OnPlayerPreparedListener
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerImpl : PlayerRepository {

    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String, listener: OnPlayerPreparedListener) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener.playerOnPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            listener.playerOnCompletion()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}