package com.example.playlistmaker.medialibrary.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.models.PlayList
import com.example.playlistmaker.common.ui.PlayListViewHolder
import com.example.playlistmaker.common.ui.PlayListsAdapter
import com.example.playlistmaker.common.utils.PLAYLIST
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.medialibrary.ui.models.PlayListsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playListsViewModel: PlayListsViewModel by viewModel()

    private val playListsAdapter = object : PlayListsAdapter(
        clickListener = {
            clickOnPlayList(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
            return PlayListViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_for_grid, parent, false)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlayLists.adapter = playListsAdapter

        binding.newPlayList.setOnClickListener {
            findNavController().navigate(
                R.id.action_to_addPlayListFragment
            )
        }

        playListsViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListsState.Empty -> {
                    binding.rvPlayLists.visibility = View.GONE
                    binding.placeholderNotFound.visibility = View.VISIBLE
                }

                is PlayListsState.PlayLists -> {
                    playListsAdapter.playLists = it.playLists
                    binding.placeholderNotFound.visibility = View.GONE
                    binding.rvPlayLists.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        playListsViewModel.requestPlayLists()
    }

    private fun clickOnPlayList(playList: PlayList) {
        findNavController().navigate(
            R.id.action_to_PlayListFragment,
            Bundle().apply {
                putSerializable(PLAYLIST, playList)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = PlayListsFragment()
    }
}