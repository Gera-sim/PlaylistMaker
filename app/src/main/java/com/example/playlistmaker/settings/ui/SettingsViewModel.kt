package com.example.playlistmaker.settings.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.api.ThemeSwitchInteractor

class SettingsViewModel(private val switchThemeInteractor: ThemeSwitchInteractor) : ViewModel() {

    fun switchTheme(isChecked: Boolean) {
        switchThemeInteractor.switch(isChecked)
    }

    fun isDarkThemeOn(): Boolean {
        return switchThemeInteractor.isDarkModeOn()
    }
   }