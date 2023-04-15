package com.example.galleryapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.galleryapp.models.Photo

class PhotoDiffUtilCallBack : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}