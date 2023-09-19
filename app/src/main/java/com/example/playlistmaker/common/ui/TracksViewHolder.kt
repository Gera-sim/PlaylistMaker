package com.example.playlistmaker.common.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val artistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val trackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    private val ivTrackPic: ImageView = itemView.findViewById(R.id.ivTrackPic)

    fun bind(track: Track) {
        trackName.text = track.trackName
        track.artistName?.let {
            artistName.text = it
        }
        if (track.trackTimeMillis != null) {
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        } else {
            trackTime.setText(R.string._00_00)
        }

        track.artworkUrl60?.let {
            Glide
                .with(itemView)
                .load(it)
                .placeholder(R.drawable.track_pic_312)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        itemView.resources.getDimensionPixelSize(
                            R.dimen.corner_radius_2
                        )
                    )
                )
                .into(ivTrackPic)
        }
    }
}