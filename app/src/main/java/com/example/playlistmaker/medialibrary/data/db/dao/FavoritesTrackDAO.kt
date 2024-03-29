package com.example.playlistmaker.medialibrary.data.db.dao

//SPR21 step 2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.medialibrary.data.db.entity.FavoritesTrackEntity

@Dao
interface FavoritesTrackDao {
    @Insert(entity = FavoritesTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(track: FavoritesTrackEntity)

    @Query("DELETE FROM track_favorites_table WHERE trackId = :trackId")
    suspend fun deleteFromFavorites(trackId: Int)

    @Query("SELECT * FROM track_favorites_table ORDER BY createdAt DESC")
    suspend fun getTracks(): List<FavoritesTrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM track_favorites_table  WHERE trackId = :trackId)")
    suspend fun isFavoriteTrack(trackId: Int): Boolean
}