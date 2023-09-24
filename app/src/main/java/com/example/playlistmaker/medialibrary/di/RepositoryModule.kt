package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.data.FavoritesRepositoryImpl
import com.example.playlistmaker.medialibrary.data.PlayListsRepositoryImpl
import com.example.playlistmaker.medialibrary.data.db.converters.PlayListsTrackDbConverter
import com.example.playlistmaker.medialibrary.data.db.converters.FavoritesTrackDbConverter
import com.example.playlistmaker.medialibrary.domain.db.FavoritesRepository
import com.example.playlistmaker.medialibrary.domain.db.PlayListsRepository
import org.koin.dsl.module

//SPR21 step 6

val mediaLibraryRepositoryModule = module {
    factory { FavoritesTrackDbConverter() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory {
        PlayListsTrackDbConverter()
    }

    single<PlayListsRepository> {
        PlayListsRepositoryImpl(
            appDatabase = get(),
            playListsTrackDbConverter = get(),
            context = get()
        )
    }
}
