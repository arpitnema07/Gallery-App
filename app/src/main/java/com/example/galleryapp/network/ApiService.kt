package com.example.galleryapp.network

import com.example.galleryapp.models.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object {
        const val BASE_URL = "https://api.flickr.com/services/rest/"
        const val API_KEY = "6f102c62f41998d151e5a1b48713cf13"
    }

    @GET("?method=flickr.photos.getRecent&extras=url_s&format=json&nojsoncallback=1")
    suspend fun getRecentPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ApiResponse

    @GET("?method=flickr.photos.search&extras=url_s&format=json&nojsoncallback=1")
    suspend fun searchPhotos(
        @Query("text") query: String,
    ): ApiResponse
}