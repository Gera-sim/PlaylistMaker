package com.example.playlistmaker.common

import android.app.Application
import com.example.playlistmaker.common.firebaseDI.firebaseAnalyticsModule
import com.example.playlistmaker.player.di.*
import com.example.playlistmaker.search.di.*
import com.example.playlistmaker.settings.di.*
import com.example.playlistmaker.medialibrary.di.*
import com.example.playlistmaker.settings.domain.api.ThemeSwitchInteractor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.time.ZonedDateTime

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
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

                firebaseAnalyticsModule
            )
        }

        val themeSwitcherInteractor: ThemeSwitchInteractor by inject()

        themeSwitcherInteractor.applyCurrentTheme()


        val analytics: FirebaseAnalytics by inject()

        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param(FirebaseAnalytics.Param.START_DATE, ZonedDateTime.now().toString())
        }

    }

}