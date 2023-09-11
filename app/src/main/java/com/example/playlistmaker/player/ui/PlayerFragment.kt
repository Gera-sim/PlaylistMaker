package com.example.playlistmaker.player.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.utils.TRACK
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerViewModel by viewModel<PlayerViewModel>()

    private lateinit var track: Track

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserveViewModel()

        showTrack(track)

        playerViewModel.preparePlayer(track.previewUrl)

        playerViewModel.isFavorite(track.trackId)

        initOnClickListeners()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        track = arguments?.getSerializable(TRACK) as Track
    }

    private fun render(state: PlayerState) {
        when (state) {
            is PlayerState.Preparing -> {
                binding.progressBar.visibility = View.VISIBLE
                }

            is PlayerState.Stopped -> {
                binding.playButton.isEnabled = true
                binding.progressBar.visibility = View.GONE
                binding.playButton.setImageResource(R.drawable.play)
                binding.playTime.setText(R.string._00_00)
            }

            is PlayerState.Paused -> {
                binding.playButton.isEnabled = true
                binding.playButton.setImageResource(R.drawable.play)
            }

            is PlayerState.Playing -> {
                binding.playButton.isEnabled = true
                binding.playButton.setImageResource(R.drawable.pause)
            }

            is PlayerState.Wait -> {
                binding.playButton.isEnabled = false
                binding.progressBar.visibility = View.GONE
                binding.playButton.setImageResource(R.drawable.play)
                binding.playTime.setText(R.string._00_00)
            }

            is PlayerState.UpdatePlayingTime -> {
                binding.playTime.text = state.playingTime
            }
            is PlayerState.StateFavorite -> {
                if (state.isFavorite) {
                    binding.addToFavorites.setImageResource(R.drawable.liked)
                } else {
                    binding.addToFavorites.setImageResource(R.drawable.like)
                }
            }


        } }

    private fun showTrack(track: Track) {
        binding.apply {
            Glide
                .with(albumPic)
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.track_pic_312)
                .centerCrop()
                .transform(
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8))
                ).into(albumPic)

            trackName.text = track.trackName
            trackName.isSelected = true

            artistName.text = track.artistName

            genreData.text = track.primaryGenreName

            countryData.text = track.country

            if (track.trackTimeMillis != null) {
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            } else {
                trackTime.setText(R.string._00_00)
            }


            val date = track.releaseDate?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
            }
            if (date != null) {
                val formattedData = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                yearData.text = formattedData
            }

            if (track.collectionName !=null) {
                collectionNameData.text = track.collectionName
            } else {
                collectionNamePlayer.visibility = View.GONE
                collectionNameData.visibility = View.GONE
            } }
        binding.playButton.isEnabled = false

    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    private fun initObserveViewModel() {
        playerViewModel.observePlayerState().observe(viewLifecycleOwner) {
            render(it)
        }
        playerViewModel.observeTrackTimeState().observe(viewLifecycleOwner) {
            render(it)
        }
        playerViewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initOnClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.addToPlaylist.setOnClickListener {
            PlayerBottomSheetFragment.newInstance(track).show(childFragmentManager, PlayerBottomSheetFragment.TAG)
        }
        binding.addToFavorites.setOnClickListener {
            playerViewModel.onFavoriteClicked(track)
        }
        binding.playButton.setOnClickListener {
            playerViewModel.playbackControl()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}