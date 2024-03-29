package com.example.playlistmaker.common.models

import java.io.Serializable

//инфо для отображения!!! Для данных с сервера используем - TrackDTO

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl60: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        return if (other !is Track) {
            false
        } else {
            other.trackId == trackId
        }
    }

    override fun hashCode(): Int {
        return trackId
    }
}