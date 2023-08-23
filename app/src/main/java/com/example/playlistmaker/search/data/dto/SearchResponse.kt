package com.example.playlistmaker.search.data.dto

//ответ от сервера : от Response
class SearchResponse(
    val results: List<TrackDTO>
) : Response()