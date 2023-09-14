package com.example.playlistmaker.medialibrary.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesTracksBinding
import com.example.playlistmaker.medialibrary.ui.models.FavoritesTracksState
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.ui.TracksAdapter
import com.example.playlistmaker.common.utils.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTracksFragment : Fragment() {

    private var binding: FragmentFavoritesTracksBinding? = null

    private val favoritesTracksViewModel: FavoritesTracksViewModel by viewModel()

    private val favoritesTracksAdapter = TracksAdapter {
        clickOnTrack(it)
    }

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
            render(it)
        }

        binding?.favoritesTracks?.adapter = favoritesTracksAdapter
    }

//            when(it) {
//                is FavoritesTracksState.Empty -> {
//                    binding?.placeholderNotFound?.visibility = View.VISIBLE
//                }
//                is FavoritesTracksState.FavoritesTracks -> {
//                }  }   }   }


    override fun onResume() {
        super.onResume()
        favoritesTracksViewModel.getFavoritesTracks()
    }

    private fun render(state: FavoritesTracksState) {
        when (state) {
            is FavoritesTracksState.Empty -> {
                binding?.favoritesTracks?.visibility = View.GONE
                binding?.placeholderNotFound?.visibility = View.VISIBLE
            }

            is FavoritesTracksState.FavoritesTracks -> {
                favoritesTracksAdapter.tracks = state.tracks
                binding?.placeholderNotFound?.visibility = View.GONE
                binding?.favoritesTracks?.visibility = View.VISIBLE
            }
        }
    }


    private fun clickOnTrack(track: Track) {
        if (favoritesTracksViewModel.clickDebounce()) {
            findNavController().navigate(
                R.id.action_to_PlayerFragment,
                Bundle().apply {
                    putSerializable(TRACK, track)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }



    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }
}