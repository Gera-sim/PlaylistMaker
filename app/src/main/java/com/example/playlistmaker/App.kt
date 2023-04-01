package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCE = "playlist_maker_preference"
const val DARK_THEME_PREFERENCE = "dark_theme_preference"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        darkTheme = darkThemeEnabled


        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
