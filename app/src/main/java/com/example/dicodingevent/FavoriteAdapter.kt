package com.example.dicodingevent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.local.FavoriteEntity
import com.example.dicodingevent.databinding.ItemRowFavoriteBinding

class FavoriteAdapter(private val listener: OnItemClickListener) : ListAdapter<FavoriteEntity, FavoriteAdapter.EventViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener {
        fun onItemClick(eventId: Int?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemRowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class EventViewHolder(private val binding: ItemRowFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEntity) {
            binding.tvEventName.text = event.name
            binding.tvEventOwner.text = event.ownerName
            Glide.with(binding.root.context).load(event.mediaCover).into(binding.imgEventLogo)

            binding.root.setOnClickListener {
                listener.onItemClick(event.id)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
