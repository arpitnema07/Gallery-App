package com.example.galleryapp.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.models.Photo
import com.example.galleryapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(application)  {

    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>> = _photos

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getRecentPhotos() {
        Log.d("TAG", "getRecentPhotos: 1")
        viewModelScope.launch {
            try {
                val photosResponse = mainRepository.getRecentPhotos(0)
                Log.d("TAG", "getRecentPhotos: "+photosResponse.photos.photo.size)
                _photos.value = photosResponse.photos.photo
            } catch (e: Exception) {
                _error.value = e.message
                e.printStackTrace()
            }
        }
    }

    fun retry() {
        getRecentPhotos()
    }

}