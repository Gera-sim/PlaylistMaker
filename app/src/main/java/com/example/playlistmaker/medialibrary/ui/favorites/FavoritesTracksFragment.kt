package com.example.playlistmaker.medialibrary.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.medialibrary.ui.models.FavoritesTracksState
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment: Fragment() {

    private var binding: FragmentFavoritesTracksBinding? = null

    private val favoritesTracksViewModel: FavoritesTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesTracksBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesTracksViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is FavoritesTracksState.Empty -> {
                    binding?.placeholderNotFound?.visibility = View.VISIBLE
                }
                is FavoritesTracksState.FavoritesTracks -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object { fun newInstance() = FavoritesTracksFragment()}
}