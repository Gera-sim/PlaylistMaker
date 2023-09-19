package com.example.playlistmaker.settings.domain.api

interface ThemeSwitchInteractor {
    fun switchTheme(isDarkThemeOn: Boolean)
    fun isDarkThemeOn(): Boolean
    fun applyCurrentTheme()
}