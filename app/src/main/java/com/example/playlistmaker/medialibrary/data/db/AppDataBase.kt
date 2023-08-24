package com.example.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.medialibrary.data.db.dao.TrackDao
import com.example.playlistmaker.medialibrary.data.db.entity.TrackEntity

//SPR21 step 3

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}