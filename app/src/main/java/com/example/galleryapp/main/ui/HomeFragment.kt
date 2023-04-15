package com.example.galleryapp.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.databinding.FragmentHomeBinding
import com.example.galleryapp.main.MainViewModel
import com.example.galleryapp.main.adapter.PhotosAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var photosAdapter: PhotosAdapter

    override fun onResume() {
        super.onResume()
        viewModel.hideSearch.value = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        photosAdapter = PhotosAdapter()

        binding.photos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = photosAdapter
        }

        lifecycleScope.launch {
            viewModel.getRecentPhotos().collectLatest { pagingData ->
                photosAdapter.submitData(pagingData)
            }
        }
        lifecycleScope.launch {
            photosAdapter.loadStateFlow.collectLatest { loadStates ->
                val isLoading = loadStates.refresh is LoadState.Loading
                val isError = loadStates.refresh is LoadState.Error
                val isListEmpty = photosAdapter.itemCount == 0

                binding.progressBar.visibility = if (isLoading && isListEmpty) View.VISIBLE else View.GONE

                if (isError) {
                    val errorState = loadStates.refresh as LoadState.Error
                    Toast.makeText(requireContext(), errorState.error.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        return binding.root
    }
}