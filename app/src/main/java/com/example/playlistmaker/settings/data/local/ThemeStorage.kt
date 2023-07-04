package com.example.playlistmaker.settings.data.local

import com.example.playlistmaker.settings.data.LocalStorage
import android.content.SharedPreferences

class ThemeStorage(private val sharedPreferences: SharedPreferences) : LocalStorage {

    override fun switch(darkThemeEnabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(DARK_MODE, darkThemeEnabled)
            .apply()
    }

    override fun isDarkModeOn(): Boolean {
        return sharedPreferences.getBoolean(DARK_MODE, false)
    }

    companion object {const val DARK_MODE = "dark_mode" }
}