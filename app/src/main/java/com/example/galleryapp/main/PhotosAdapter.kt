package com.example.galleryapp.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.galleryapp.databinding.ItemPhotoBinding
import com.example.galleryapp.models.Photo

class PhotosAdapter() :
    ListAdapter<Photo,PhotosAdapter.ViewHolder>(PhotoDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(photo: Photo){
            binding.photo = photo
            binding.executePendingBindings()
        }

    }
}

class PhotoDiffUtilCallBack : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}