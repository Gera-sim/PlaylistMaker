package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.data.dto.SearchResponse
import com.example.playlistmaker.search.data.local.LocalStorage
import com.example.playlistmaker.search.domain.api.SearchRepository
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.utils.RESULT_CODE_EMPTY
import com.example.playlistmaker.common.utils.RESULT_CODE_SUCCESS
import com.example.playlistmaker.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage
) : SearchRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))

        when (response.resultCode){
            RESULT_CODE_EMPTY -> {
                emit(Resource.Error(response.resultCode))
            }

            RESULT_CODE_SUCCESS -> {
                val tracks: List<Track> = (response as SearchResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl60,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl,
                    )
                }
                emit(Resource.Success(tracks))
            }

            else -> {
                emit(Resource.Error(response.resultCode))
            }
        }}

    override fun addTracksHistory(track: Track) { localStorage.addTracksHistory(track)}

    override fun clearTracksHistory() { localStorage.clearTracksHistory()}

    override fun getTracksHistory(): List<Track> { return localStorage.getTracksHistory()}
}