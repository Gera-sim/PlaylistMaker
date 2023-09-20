package com.example.playlistmaker.medialibrary.data

import com.example.playlistmaker.medialibrary.data.db.AppDatabase
import com.example.playlistmaker.medialibrary.data.db.converters.FavoritesTrackDbConverter
import com.example.playlistmaker.medialibrary.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.medialibrary.domain.db.FavoritesRepository
import com.example.playlistmaker.common.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//SPR21 step 8

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoritesTrackDbConverter: FavoritesTrackDbConverter
) : FavoritesRepository {
    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        emit(convertFromTrackEntity(
            appDatabase.favoritesTrackDao().getTracks())
        )
    }

    override fun isFavoriteTrack(trackId: Int): Flow<Boolean> = flow {
        emit(
            appDatabase.favoritesTrackDao().isFavoriteTrack(trackId)
        )
    }

    override suspend fun addToFavorites(track: Track) =
        appDatabase.favoritesTrackDao().addToFavorites(
            favoritesTrackDbConverter.map(track)
        )

    override suspend fun deleteFromFavorites(trackId: Int) =
        appDatabase.favoritesTrackDao().deleteFromFavorites(trackId)

    private fun convertFromTrackEntity(tracks: List<FavoritesTrackEntity>): List<Track> =
        tracks.map { track -> favoritesTrackDbConverter.map(track) }

}