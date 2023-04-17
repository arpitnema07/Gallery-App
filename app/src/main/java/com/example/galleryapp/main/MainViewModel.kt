package com.example.galleryapp.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.galleryapp.models.Photo
import com.example.galleryapp.models.Result
import com.example.galleryapp.network.PhotosPagingSource
import com.example.galleryapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val pagingSource: PhotosPagingSource,
    private val mainRepository: MainRepository
) : AndroidViewModel(application)  {

    val hideSearch: MutableLiveData<Boolean> = MutableLiveData(false)

//    private val _isLoading = MutableLiveData<Boolean>()

    fun getRecentPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                maxSize = 3,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = { pagingSource }
        ).flow.cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) = flow {
        try {
            emit(Result.Loading)
            val response = withTimeoutOrNull(5000) {
                mainRepository.searchPhotos(query)
            }
            if (response?.isSuccessful == true && response.body() != null) {
                val apiResponse = response.body()!!.photos.photo
                emit(Result.Success(apiResponse))
            } else {
                val errorMessage = response?.errorBody()?.string() ?: "Unknown error"
                emit(Result.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Result.Error("Exception occurred: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

}