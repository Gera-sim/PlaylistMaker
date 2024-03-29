package com.example.playlistmaker.search.data.dto

//Модель для данных с сервера

class TrackDTO (
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
)