package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.ThemeSwitchInteractor
import com.example.playlistmaker.settings.domain.api.ThemeSwitchRepository

class ThemeSwitchInteractorImpl(private val themeSwitchRepository: ThemeSwitchRepository) :
    ThemeSwitchInteractor {
    override fun switchTheme(isDarkThemeOn: Boolean) {
        themeSwitchRepository.switchTheme(isDarkThemeOn)
    }

    override fun isDarkThemeOn(): Boolean {
        return themeSwitchRepository.isDarkThemeOn()
    }

    override fun applyCurrentTheme() {
        themeSwitchRepository.applyCurrentTheme()
    }
}