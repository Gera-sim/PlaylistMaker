package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.domain.db.FavoritesInteractor
import com.example.playlistmaker.medialibrary.domain.db.PlayListsInteractor
import com.example.playlistmaker.medialibrary.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.medialibrary.domain.impl.PlayListsInteractorImpl
import org.koin.dsl.module

//SPR21 step 12

val mediaLibraryInteractorModule = module {
    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
    single<PlayListsInteractor> {
        PlayListsInteractorImpl(get())
    }
}