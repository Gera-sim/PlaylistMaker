package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.models.PlayList
import com.example.playlistmaker.common.ui.PlayListViewHolder
import com.example.playlistmaker.common.ui.PlayListsAdapter
import com.example.playlistmaker.databinding.FragmentPlayerBottomSheetBinding
import com.example.playlistmaker.player.ui.models.PlayListsState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerBottomSheetFragment(val track: Track) : BottomSheetDialogFragment() {

    private var _binding: FragmentPlayerBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModelPlayerBottomSheet by viewModel<PlayerBottomSheetViewModel>()

    private val playListsAdapter = object : PlayListsAdapter(
        clickListener = {
            addTrackToPlayList(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
            return PlayListViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_playlist_bottom_sheet, parent, false)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistsRecycler.adapter = playListsAdapter

        viewModelPlayerBottomSheet.observePlayListsState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayListsState.Empty -> binding.playlistsRecycler.visibility = View.GONE
                is PlayListsState.PlayLists -> {
                    playListsAdapter.playLists = it.playLists
                    binding.playlistsRecycler.visibility = View.VISIBLE
                }

                is PlayListsState.AddTrackToPlayListResult -> {
                    if (it.isAdded) {
                        showToast(getString(R.string.added_to_playlist, it.playListName))
                        dismiss()
                    } else {
                        showToast(getString(R.string.already_added_to_playlist, it.playListName))
                    }
                }
            }
        }

        binding.createPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_to_addPlayListFragment
            )
        }

    }

    override fun onResume() {
        super.onResume()
        viewModelPlayerBottomSheet.requestPlayLists()
    }

    private fun addTrackToPlayList(playList: PlayList) {
        if (viewModelPlayerBottomSheet.clickDebounce()) {
            viewModelPlayerBottomSheet.addTrackToPlayList(track, playList)
        }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "PlayerBottomSheet"

        fun newInstance(track: Track): PlayerBottomSheetFragment {
            return PlayerBottomSheetFragment(track)
        }
    }
}