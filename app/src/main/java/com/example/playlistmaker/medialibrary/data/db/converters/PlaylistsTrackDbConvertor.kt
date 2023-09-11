package com.example.playlistmaker.medialibrary.data.db.converters

import com.example.playlistmaker.common.models.PlayList
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListEntity
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListWithCountTracks
import com.example.playlistmaker.medialibrary.data.db.entity.PlayListsTrackEntity
import java.util.Calendar

class PlayListsTrackDbConvertor {

    fun map(playList: PlayList): PlayListEntity {
        playList.apply {
            return PlayListEntity(
                null,
                name,
                description,
                image,
                Calendar.getInstance().timeInMillis
            )
        }
    }

    fun map(playListWithCountTracks: PlayListWithCountTracks): PlayList {
        playListWithCountTracks.apply {
            return PlayList(
                playListId!!,
                name,
                description,
                image,
                tracksCount,
            )
        }
    }

    fun map(playListsTrackEntity: PlayListsTrackEntity): Track {
        playListsTrackEntity.apply {
            return Track(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl
            )
        }
    }

    fun map(track: Track): PlayListsTrackEntity {
        track.apply {
            return PlayListsTrackEntity(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl,
            )
        }
    }
}