package com.example.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListEntity
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListWithCountTracks
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListsTrackEntity
import com.example.playlistmaker.medialibrary.data.db.entity.TrackPlayListEntity

@Dao
interface PlayListsDao {

    @Insert(entity = PlayListEntity::class)
    suspend fun addPlayList(playList: PlayListEntity)

    @Insert(entity = PlayListsTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlayListsTrack(playListsTrackEntity: PlayListsTrackEntity)
    @Insert(entity = TrackPlayListEntity::class)
    suspend fun addTrackPlayList(trackPlayListEntity: TrackPlayListEntity)
    @Transaction
    suspend fun addTrackToPlayList(
        playListsTrackEntity: PlayListsTrackEntity,
        trackPlayListEntity: TrackPlayListEntity
    ) {
        addPlayListsTrack(playListsTrackEntity)
        addTrackPlayList(trackPlayListEntity)
    }

    @Query("SELECT playListId, name, description, image, (SELECT COUNT(id) FROM play_lists_track_table WHERE play_lists_track_table.playListId=play_lists_table.playListId) as tracksCount FROM play_lists_table ORDER BY playListId DESC")
    suspend fun getPlayLists(): List<PlayListWithCountTracks>

    @Query("SELECT track_play_lists_table.* FROM track_play_lists_table LEFT JOIN play_lists_track_table ON track_play_lists_table.trackId=play_lists_track_table.trackId WHERE play_lists_track_table.playListId = :playListId  ORDER BY play_lists_track_table.id DESC")
    suspend fun getPlayListTracks(playListId: Int): List<PlayListsTrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM play_lists_track_table  WHERE trackId = :trackId AND playListId = :playListId)")
    suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean


}