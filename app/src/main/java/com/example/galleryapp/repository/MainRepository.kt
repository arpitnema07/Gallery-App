package com.example.galleryapp.repository

import com.example.galleryapp.models.ApiResponse
import com.example.galleryapp.network.MainApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val mainApiService: MainApiService) {

    suspend fun getRecentPhotos(page : Int): ApiResponse{
        return mainApiService.getRecentPhotos(page)
    }
    suspend fun searchPhotos(query: String): ApiResponse{
        return mainApiService.searchPhotos(query)
    }
}