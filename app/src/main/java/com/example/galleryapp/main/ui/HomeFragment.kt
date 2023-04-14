package com.example.galleryapp.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.galleryapp.databinding.FragmentHomeBinding
import com.example.galleryapp.main.MainViewModel
import com.example.galleryapp.main.PhotosAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var photosAdapter: PhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        photosAdapter = PhotosAdapter()

        binding.photos.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
            this.adapter = photosAdapter
        }
        viewModel.photos.observe(viewLifecycleOwner) { list ->
            list?.let {
                photosAdapter.submitList(list)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
                    .setAction("Retry") { viewModel.retry() }
                    .show()
            }
        }
        return binding.root
    }
}