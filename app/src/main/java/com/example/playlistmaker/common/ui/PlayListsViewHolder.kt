package com.example.playlistmaker.common.ui

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.common.models.PlayList
import com.example.playlistmaker.common.utils.PLAYLISTS_IMAGES_DIRECTORY
import java.io.File

class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playListImage: ImageView = itemView.findViewById(R.id.ivPlayListImage)
    private val playListName: TextView = itemView.findViewById(R.id.playListName)
    private val playListCountTracks: TextView = itemView.findViewById(R.id.tv_playlist_tracks_count)

    fun bind(playList: PlayList) {
        playListName.text = playList.name
        playListCountTracks.text = playListCountTracks.resources.getQuantityString(
            R.plurals.plural_count_tracks, playList.tracksCount, playList.tracksCount
        )
        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLISTS_IMAGES_DIRECTORY
        )

        Glide
            .with(itemView)
            .load(playList.image?.let { imageName -> File(filePath, imageName) })
            .placeholder(R.drawable.track_pic_312)
            .into(playListImage)
    }
}