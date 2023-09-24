package com.example.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.medialibrary.data.db.dao.FavoritesTrackDao
import com.example.playlistmaker.medialibrary.data.db.dao.PlayListsDao
import com.example.playlistmaker.medialibrary.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListEntity
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListsTrackEntity
import com.example.playlistmaker.medialibrary.data.db.entity.TrackPlayListEntity

//SPR21 step 3

@Database(
    version = 4,
    entities = [FavoritesTrackEntity::class, PlayListsTrackEntity::class, PlayListEntity::class, TrackPlayListEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesTrackDao(): FavoritesTrackDao

    abstract fun playListsTrackDao(): PlayListsDao
}