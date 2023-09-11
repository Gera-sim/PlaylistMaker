package com.example.playlistmaker.medialibrary.data.db.converters

import com.example.playlistmaker.medialibrary.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.common.models.Track
import java.util.Calendar

//SPR21 step 4

class FavoritesTrackDbConvertor {
    fun map(track: Track): FavoritesTrackEntity {
        track.apply {
            return FavoritesTrackEntity(
                track.trackId,
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl,
                Calendar.getInstance().timeInMillis
            )
        }
    }

    fun map(track: FavoritesTrackEntity): Track {
        track.apply {
            return Track(
                track.trackId,
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl
            )
        }
    }
}