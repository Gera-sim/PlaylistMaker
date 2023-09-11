package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.common.utils.RESULT_CODE_BAD_REQUEST
import com.example.playlistmaker.common.utils.RESULT_CODE_EMPTY
import com.example.playlistmaker.common.utils.RESULT_CODE_ERROR
import com.example.playlistmaker.common.utils.RESULT_CODE_SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesAPI: ITunesAPI,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = RESULT_CODE_EMPTY }
        }
        if (dto !is SearchRequest) {
            return Response().apply { resultCode = RESULT_CODE_BAD_REQUEST }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = iTunesAPI.search(dto.expression)
                response.apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = RESULT_CODE_ERROR }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}