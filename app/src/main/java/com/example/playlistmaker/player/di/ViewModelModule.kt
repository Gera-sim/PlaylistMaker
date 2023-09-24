package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.player.ui.PlayerBottomSheetViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {
    viewModel {
        PlayerViewModel(playerInteractor = get(), favoritesInteractor = get(), analytics = get())
    }

    viewModel {
        PlayerBottomSheetViewModel(playListsInteractor = get())
    }

}