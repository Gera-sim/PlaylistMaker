package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        findViewById<ImageView>(R.id.back_button_player_page).setOnClickListener { finish() }

        findViewPlayer()

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
            collectionNamePlayer.text = track.collectionName
        } else {
            collectionNamePlayer.visibility = View.GONE
            collectionNameData.visibility = View.GONE
        }
    }

    private fun findViewPlayer() {
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.playing_time)
        albumPic = findViewById(R.id.albumPic)
        collectionNamePlayer = findViewById(R.id.collectionNamePlayer)
        collectionNameData = findViewById(R.id.collectionNameData)
        releaseDate = findViewById(R.id.yearPlayer)
        primaryGenreName = findViewById(R.id.genrePlayer)
        country = findViewById(R.id.countryPlayer)
    }
}