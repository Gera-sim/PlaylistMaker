package com.example.playlistmaker.search

import android.icu.text.SimpleDateFormat //Не понятно Android или Java вариант импортить
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import java.util.*

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val artistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.ivTrackArt)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        Glide
            .with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.track_pic)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_pic_corner_radius)))
            .centerCrop()
            .into(artworkUrl100)
    }
}