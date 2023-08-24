package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.data.FavoritesRepositoryImpl
import com.example.playlistmaker.medialibrary.data.db.converters.TrackDbConvertor
import com.example.playlistmaker.medialibrary.domain.db.FavoritesRepository
import org.koin.dsl.module

//SPR21 step 6

val mediaLibraryRepositoryModule = module {
    factory { TrackDbConvertor() }

    //SPR21 step 9

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }
}