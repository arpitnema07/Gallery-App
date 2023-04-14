package com.example.galleryapp.di

import com.example.galleryapp.network.MainApiService
import com.example.galleryapp.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Provides
    @Singleton
    fun provideMainApiService(): MainApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.flickr.com/services/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MainApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainRepository(mainApiService: MainApiService): MainRepository {
        return MainRepository(mainApiService)
    }
}
