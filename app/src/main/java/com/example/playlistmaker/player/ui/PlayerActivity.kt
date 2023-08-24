package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.ui.models.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observePlayerState().observe(this)
        {render(it)}

        viewModel.observeTrackTimeState().observe(this) {
            render(it)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.observeFavoriteState().observe(this) {
            render(it)
        }

//        новый метод getSerializableExtra(String, Class)
//        предоставляет более безопасную версию по типам.
//        Он позволяет передать класс ожидаемого объекта в качестве второго аргумента.
//
//        minApi=33
//
//       val track = intent.getSerializableExtra(TRACK, Track::class.java) as Track

      val track = intent.getSerializableExtra(TRACK) as Track

        showTrack(track)

        viewModel.isFavorite(track.trackId)

        binding.addToFavorites.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        binding.playButton.isEnabled = false

        if (savedInstanceState==null) {
            viewModel.preparePlayer(track.previewUrl)
        }

        binding.playButton.setOnClickListener { viewModel.playbackControl() }
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
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.track_pic)
                .centerCrop()
                .transform(
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_track_pic_corner_radius))
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

            if (track.collectionName.isNotEmpty()) {
                collectionNameData.text = track.collectionName
            } else {
                collectionNamePlayer.visibility = View.GONE
                collectionNameData.visibility = View.GONE
            } } }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }
}