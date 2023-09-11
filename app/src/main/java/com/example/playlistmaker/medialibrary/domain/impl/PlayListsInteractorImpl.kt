package com.example.playlistmaker.medialibrary.domain.impl

import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.models.PlayList
import com.example.playlistmaker.medialibrary.domain.db.PlayListsInteractor
import com.example.playlistmaker.medialibrary.domain.db.PlayListsRepository

class PlayListsInteractorImpl(private val playListsRepository: PlayListsRepository) :
    PlayListsInteractor {
    override suspend fun addPlayList(playList: PlayList) =
        playListsRepository.addPlayList(playList)

    override suspend fun addTrackToPlayList(track: Track, playListId: Int) =
        playListsRepository.addTrackToPlayList(track, playListId)

    override suspend fun getPlayLists(): List<PlayList> =
        playListsRepository.getPlayLists()

    override suspend fun getPlayListTracks(playListId: Int): List<Track> =
        playListsRepository.getPlayListTracks(playListId)

    override suspend fun isTrackInPlayList(trackId: Int, playListId: Int): Boolean =
        playListsRepository.isTrackInPlayList(trackId, playListId)

}