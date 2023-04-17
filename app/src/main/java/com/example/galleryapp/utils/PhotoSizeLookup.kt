package com.example.galleryapp.utils

import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.R
import com.example.galleryapp.main.adapter.PhotosAdapter

class PhotosSpanSizeLookup(private val adapter: PhotosAdapter) :
    GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        return if (adapter.getItemViewType(position) == R.layout.item_photo_load_state) 2 else 1
    }
}
