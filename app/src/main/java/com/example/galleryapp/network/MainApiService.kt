package com.example.galleryapp.network

import com.example.galleryapp.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApiService {

    companion object {

//        const val API_KEY = "6f102c62f41998d151e5a1b48713cf13"
        const val API_KEY = "ef12d2002b65b7f4a52e99f90ed1e1c7"
    }

    @GET("?method=flickr.photos.getRecent&extras=url_s&api_key=${API_KEY}&per_page=20&format=json&nojsoncallback=1")
    suspend fun getRecentPhotos(
        @Query("page") page: Int
    ): Response<ApiResponse>

    @GET("?method=flickr.photos.search&extras=url_s&api_key=${API_KEY}&format=json&nojsoncallback=1")
    suspend fun searchPhotos(
        @Query("text") query: String,
    ): Response<ApiResponse>
}