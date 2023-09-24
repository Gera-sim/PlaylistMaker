package com.example.playlistmaker.common.firebaseDI

import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.dsl.module

val firebaseAnalyticsModule = module {
    single {
        FirebaseAnalytics.getInstance(get())
    }
}