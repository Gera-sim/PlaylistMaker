package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var trackName: TextView
    private lateinit var trackTime: TextView
    private lateinit var artistName: TextView
    private lateinit var albumPic: ImageView
    private lateinit var collectionNamePlayer: TextView  //для обработки скрытия при отсутствии данных
    private lateinit var collectionNameData: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var playButton: ImageView
    private lateinit var playTime: TextView
    private lateinit var progressBar: ProgressBar
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    private val updatePlayingTimeRunnable = Runnable { updatePlayingTime() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)



        findViewById<ImageView>(R.id.back_button_player_page).setOnClickListener { finish() }

        findViewPlayer()

        preparePlayer(track.previewUrl)

        playButton.isEnabled = false
        playButton.setOnClickListener { playbackControl() }


        Glide
            .with(albumPic)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_pic)
            .centerCrop()
            .transform(
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_track_pic_corner_radius))
            ).into(albumPic)

        trackName.text = track.trackName
        artistName.text = track.artistName
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val date = track.releaseDate?.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
        }
        if (date != null) {
            val formattedData = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            releaseDate.text = formattedData
        }


        if (track.collectionName.isNotEmpty()) {
            collectionNameData.text = track.collectionName
        } else {
            collectionNamePlayer.visibility = View.GONE
            collectionNameData.visibility = View.GONE
        }

    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playButton.setImageResource(R.drawable.play)
            progressBar.visibility = View.GONE
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            playTime.setText(R.string._00_00)
            handler.removeCallbacks(updatePlayingTimeRunnable)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        updatePlayingTime()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun updatePlayingTime() {
        playTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        handler.postDelayed(updatePlayingTimeRunnable, UPDATE_PLAYING_TIME_DELAY)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }


    private fun findViewPlayer() {
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.trackTime)
        albumPic = findViewById(R.id.albumPic)
        collectionNamePlayer = findViewById(R.id.collectionNamePlayer)
        collectionNameData = findViewById(R.id.collectionNameData)
        releaseDate = findViewById(R.id.yearData)
        primaryGenreName = findViewById(R.id.genreData)
        country = findViewById(R.id.countryData)
        playButton = findViewById(R.id.playButton)
        playTime = findViewById(R.id.playTime)
        progressBar = findViewById(R.id.progressBar)
    }
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE_PLAYING_TIME_DELAY = 500L
    }


}