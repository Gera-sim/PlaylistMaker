package com.example.playlistmaker.common.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.utils.DiffCallback
import com.example.playlistmaker.search.ui.SearchViewHolder

class TracksAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<SearchViewHolder>() {

    var tracks = listOf<Track>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                object : DiffCallback<Track>(field, newList) {
                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return field[oldItemPosition].trackId == newList[newItemPosition].trackId
                    }
                }

            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[holder.adapterPosition]) }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}