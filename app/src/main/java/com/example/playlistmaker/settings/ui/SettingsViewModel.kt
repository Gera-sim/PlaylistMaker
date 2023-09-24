package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.ThemeSwitchInteractor

class SettingsViewModel(private val switchThemeInteractor: ThemeSwitchInteractor) : ViewModel() {

    fun switchTheme(isChecked: Boolean) {
        switchThemeInteractor.switchTheme(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return switchThemeInteractor.isDarkThemeOn()
    }
   }