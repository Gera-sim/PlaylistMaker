package com.example.playlistmaker.search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistorySharedPref(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val HISTORY = "history"
    }

    private fun save(history: MutableList<Track>) {
        val json = Gson().toJson(history)
        sharedPreferences.edit { putString(HISTORY, json) }
    }

    fun add(track: Track) {
        val history = get()
        history.remove(track)
        history.add(0, track)
        if (history.size > 10) history.removeLast()
        save(history)
    }

    fun get(): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY, null) ?: return arrayListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }

    fun clear() {
        sharedPreferences.edit { remove(HISTORY) }
    }
}