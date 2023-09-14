package com.example.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//SPR21 step 1

@Entity(tableName = "track_favorites_table")
data class FavoritesTrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val createdAt: Long // поле для сортировки
)