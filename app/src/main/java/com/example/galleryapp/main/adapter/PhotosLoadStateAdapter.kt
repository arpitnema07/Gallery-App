package com.example.galleryapp.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.galleryapp.databinding.ItemPhotoLoadStateBinding

class PhotosLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PhotosLoadStateAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemPhotoLoadStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.errorMessage.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.errorMessage.isVisible = loadState is LoadState.Error

            if (loadState is LoadState.Error) {
                binding.errorMessage.text = loadState.error.localizedMessage
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoLoadStateBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
