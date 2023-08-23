package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.player.di.*
import com.example.playlistmaker.search.di.*
import com.example.playlistmaker.settings.di.*
import com.example.playlistmaker.medialibrary.di.*
import com.example.playlistmaker.settings.domain.api.ThemeSwitchInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)

            modules(
                playerDataModule,
                playerRepositoryModule,
                playerInteractorModule,
                playerViewModelModule,

                searchDataModule,
                searchRepositoryModule,
                searchInteractorModule,
                searchViewModelModule,

                settingsDataModule,
                settingsRepositoryModule,
                settingsInteractorModule,
                settingsViewModelModule,

                mediaLibraryViewModelsModule,
                mediaLibraryDataModule,
                mediaLibraryInteractorModule,
                mediaLibraryRepositoryModule,
            )
        }

        val themeSwitcherInteractor: ThemeSwitchInteractor by inject()

        themeSwitcherInteractor.applyCurrentTheme()
    }
}