package com.example.playlistmaker.medialibrary.di

import com.example.playlistmaker.medialibrary.ui.favorites.FavoritesTracksViewModel
import com.example.playlistmaker.medialibrary.ui.playlists.AddPlayListViewModel
import com.example.playlistmaker.medialibrary.ui.playlists.PlayListBottomSheetViewModel
import com.example.playlistmaker.medialibrary.ui.playlists.PlayListViewModel
import com.example.playlistmaker.medialibrary.ui.playlists.PlayListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// SPR19

val mediaLibraryViewModelsModule = module {
    viewModel {
        FavoritesTracksViewModel(favoritesInteractor = get())
    }
    viewModel {
        PlayListsViewModel(playListsInteractor = get())
    }
    viewModel {
        PlayListViewModel(playListsInteractor = get())
    }
    viewModel {
        AddPlayListViewModel(playListsInteractor = get())
    }
    viewModel {
        PlayListBottomSheetViewModel(
            playListsInteractor = get()
        )
    }
}