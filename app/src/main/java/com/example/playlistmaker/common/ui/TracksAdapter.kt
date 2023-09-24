package com.example.playlistmaker.common.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.common.models.Track
import com.example.playlistmaker.common.utils.DiffCallback

class TracksAdapter(
    private val clickListener: TrackClickListener,
    private val longClickListener: LongTrackClickListener? = null
    ) :
    RecyclerView.Adapter<TracksViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(tracks[holder.adapterPosition])
        }
        longClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                listener.onTrackLongClick(tracks[holder.adapterPosition])
                return@setOnLongClickListener true
            }
        }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun interface LongTrackClickListener {
        fun onTrackLongClick(track: Track)
    }
}