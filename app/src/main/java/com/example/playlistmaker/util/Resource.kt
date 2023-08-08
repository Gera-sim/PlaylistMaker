package com.example.playlistmaker.util

sealed class Resource<T>(val data: T? = null, val errorState: Int? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorState: Int, data: T? = null) : Resource<T>(data, errorState)
}