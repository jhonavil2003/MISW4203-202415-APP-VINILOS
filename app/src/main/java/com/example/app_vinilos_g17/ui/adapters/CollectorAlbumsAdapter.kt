package com.example.app_vinilos_g17.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_vinilos_g17.databinding.ItemAlbumBinding
import com.example.app_vinilos_g17.models.CollectorAlbum

class CollectorAlbumsAdapter(private var collectorAlbums: List<CollectorAlbum>) :
    RecyclerView.Adapter<CollectorAlbumsAdapter.CollectorAlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAlbumViewHolder {
        val binding =
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectorAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorAlbumViewHolder, position: Int) {
        holder.bind(collectorAlbums[position])
    }

    override fun getItemCount(): Int = collectorAlbums.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateCollectorAlbums(newCollectorAlbums: List<CollectorAlbum>) {
        collectorAlbums = newCollectorAlbums
        notifyDataSetChanged()
    }

    inner class CollectorAlbumViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(collectorAlbum: CollectorAlbum) {
            binding.textViewAlbumName.text = collectorAlbum.albumName ?: "Sin nombre"
            binding.textViewAlbumPrice.text = "$${collectorAlbum.price}"
            binding.textViewAlbumStatus.text = collectorAlbum.status
        }
    }
}
