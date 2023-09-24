package com.example.playlistmaker.common.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.common.utils.DiffCallback
import com.example.playlistmaker.common.models.PlayList

abstract class PlayListsAdapter(private val clickListener: PlayListClickListener) :
    RecyclerView.Adapter<PlayListViewHolder>() {

    var playLists = listOf<PlayList>()
        set(newList) {
            val diffResult = DiffUtil.calculateDiff(
                object : DiffCallback<PlayList>(field, newList) {
                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        return field[oldItemPosition].playListId == newList[newItemPosition].playListId
                                && field[oldItemPosition].image == newList[newItemPosition].image
                                && field[oldItemPosition].name == newList[newItemPosition].name
                                && field[oldItemPosition].tracksCount == newList[newItemPosition].tracksCount
                    }
                }
            )
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder

    override fun getItemCount(): Int = playLists.size

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playLists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(playLists[holder.adapterPosition])
        }
    }

    fun interface PlayListClickListener {
        fun onClick(playList: PlayList)
    }
}