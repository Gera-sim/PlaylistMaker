package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.db.FavoritesInteractor
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.common.models.Track
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel (
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val analytics: FirebaseAnalytics
): ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()

    private val trackTimeStateLiveData = MutableLiveData<PlayerState.UpdatePlayingTime>()

    private val trackFavoriteStateLiveData = MutableLiveData<PlayerState.StateFavorite>()

    private var timerJob: Job? = null

    private var isTrackFavorite = false

    private var isPlayerPrepared = false

    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData
    fun observeTrackTimeState(): LiveData<PlayerState.UpdatePlayingTime> = trackTimeStateLiveData

    fun observeFavoriteState(): LiveData<PlayerState.StateFavorite> = trackFavoriteStateLiveData

//    private val updatePlayingTimeRunnable = Runnable { updatePlayingTime() }

    fun preparePlayer(url: String?) {
        if (!isPlayerPrepared) {
            isPlayerPrepared = true
        renderState(PlayerState.Preparing)
        if (url != null) {
            playerInteractor.preparePlayer(
                url = url,
                onPreparedListener = {
                    renderState(PlayerState.Stopped)
                },
                onCompletionListener = {
                    timerJob?.cancel()
                    renderState(PlayerState.Stopped)
                }
            )
        } else {
            renderState(PlayerState.Wait)
        }}
    }

    override fun onCleared() {
        super.onCleared()
        isPlayerPrepared = false
        playerInteractor.releasePlayer()
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerState.Playing)
        updatePlayingTime()
    }

    fun pausePlayer() {
        if (isPlaying()) {
        playerInteractor.pausePlayer()
        renderState(PlayerState.Paused)
        timerJob?.cancel()
    }}

    private fun isPlaying(): Boolean {
        return playerInteractor.isPlaying()
    }

    private fun getCurrentPosition(): Int {
        return playerInteractor.getCurrentPosition()
    }

    fun playbackControl() {
        if (isPlaying()) {pausePlayer()}
        else {startPlayer()}
    }

    private fun updatePlayingTime() {
        timerJob = viewModelScope.launch {
            while (isPlaying()) {
                delay(UPDATE_PLAYING_TIME_DELAY_MILLIS)
                trackTimeStateLiveData.postValue(
                    PlayerState.UpdatePlayingTime(
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(
                            getCurrentPosition()
                        )
                    )
                )
            }
        }
    }

    private fun renderState(state: PlayerState) {
        playerStateLiveData.postValue(state)
    }

    fun isFavorite(trackId: Int) {
        viewModelScope.launch {
            favoritesInteractor
                .isFavoriteTrack(trackId)
                .collect { isFavorite ->
                    isTrackFavorite = isFavorite
                    trackFavoriteStateLiveData.postValue(
                        PlayerState.StateFavorite(isFavorite)
                    )
                }
        }
    }


    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            isTrackFavorite = if (isTrackFavorite) {
                favoritesInteractor.deleteFromFavorites(track.trackId)
                trackFavoriteStateLiveData.postValue(
                    PlayerState.StateFavorite(false)
                )
                false
            } else {


                analytics.logEvent("Add_track_to_favorites") {
                    track.artistName?.let { param("Artist_name", it) }
                    param("Track_name", track.trackName)
                }
                favoritesInteractor.addToFavorites(track)
                trackFavoriteStateLiveData.postValue(
                    PlayerState.StateFavorite(true)
                )
                true
            }
        }
    }


    companion object {
        //SPR21 500->300
        private const val UPDATE_PLAYING_TIME_DELAY_MILLIS = 300L
    }
}