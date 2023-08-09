package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.data.local.LocalStorage
import com.example.playlistmaker.search.domain.api.SearchRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.RESULT_CODE_EMPTY
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage
) : SearchRepository {

    override fun searchTracks(expression: String): Flow<Resource<ArrayList<Track>>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))

        when (response.resultCode){
            RESULT_CODE_EMPTY -> {
                emit(Resource.Error(response.resultCode))
            }

            200 -> {
                val arrayListTracks = arrayListOf<Track>()
                (response as SearchResponse).results.forEach {
                    arrayListTracks.add(
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl,
                        ) )  }
                emit(Resource.Success(arrayListTracks))
            }

            else -> { emit(Resource.Error(response.resultCode)) } } }

    override fun addTracksHistory(track: Track) { localStorage.addTracksHistory(track)}

    override fun clearTracksHistory() { localStorage.clearTracksHistory()}

    override fun getTracksHistory(): ArrayList<Track> { return localStorage.getTracksHistory()}
}