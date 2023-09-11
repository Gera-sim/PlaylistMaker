package com.example.playlistmaker.medialibrary.domain.db

import com.example.playlistmaker.common.models.Track
import kotlinx.coroutines.flow.Flow

//SPR21 step 7

interface FavoritesRepository {

    fun getFavoritesTracks(): Flow<List<Track>>

    fun isFavoriteTrack(trackId: Int): Flow<Boolean>

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(trackId: Int)
}