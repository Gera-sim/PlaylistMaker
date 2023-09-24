package com.example.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListEntity
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListWithCountTracks
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListsTrackEntity
import com.example.playlistmaker.medialibrary.data.db.entity.TrackPlayListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListsDao {
    @Insert(entity = PlayListEntity::class)
    suspend fun addPlayList(playList: PlayListEntity)

    @Update(entity = PlayListEntity::class)
    suspend fun editPlayList(playList: PlayListEntity)

    @Insert(
        entity = PlayListsTrackEntity::class,
        onConflict = OnConflictStrategy.IGNORE
    ) // добавляет трек
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

    @Query("SELECT playListId, name, description, image, (SELECT COUNT(id) FROM playlists_track_table WHERE playlists_track_table.playListId=playlists_table.playListId) as tracksCount FROM playlists_table WHERE playListId = :playListId")
    suspend fun getPlayList(playListId: Int): PlayListWithCountTracks

    @Query("SELECT playListId, name, description, image, (SELECT COUNT(id) FROM playlists_track_table WHERE playlists_track_table.playListId=playlists_table.playListId) as tracksCount FROM playlists_table ORDER BY playListId DESC")
    suspend fun getPlayLists(): List<PlayListWithCountTracks>

    @Query("SELECT track_playlists_table.* FROM track_playlists_table LEFT JOIN playlists_track_table ON track_playlists_table.trackId=playlists_track_table.trackId WHERE playlists_track_table.playListId = :playListId  ORDER BY playlists_track_table.id DESC")
    suspend fun getPlayListTracks(playListId: Int): List<PlayListsTrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM playlists_track_table  WHERE trackId = :trackId AND playListId = :playListId)")
    suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean

    @Query("DELETE FROM track_playlists_table WHERE trackId NOT IN (SELECT DISTINCT(trackId) FROM playlists_track_table)")
    suspend fun clearTracks()

    @Query("DELETE FROM playlists_track_table WHERE playListId = :playListId AND trackId = :trackId")
    suspend fun deleteTrackFromTrackPlayList(playListId: Int, trackId: Int)

    @Transaction
    suspend fun deleteTrackFromPlayList(
        trackId: Int,
        playListId: Int
    ) {
        deleteTrackFromTrackPlayList(playListId, trackId)
        clearTracks()
    }

    @Query("DELETE FROM playlists_table WHERE playListId = :playListId")
    suspend fun deletePlayListFromPlayList(playListId: Int)

    @Query("DELETE FROM playlists_track_table WHERE playListId = :playListId")
    suspend fun deletePlayListFromTrackPlayList(playListId: Int)

    @Transaction
    suspend fun deletePlayList(playListId: Int) {
        deletePlayListFromPlayList(playListId)
        deletePlayListFromTrackPlayList(playListId)
        clearTracks()
    }

    @Query("SELECT COUNT(*) FROM playlists_track_table WHERE playListId = :playListId")
    fun getTrackCountForPlaylist(playListId: Int): Flow<Int>
}