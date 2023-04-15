package com.example.galleryapp.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.galleryapp.models.Photo
import com.example.galleryapp.repository.MainRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val STARTING_PAGE_INDEX = 1

class PhotosPagingSource @Inject constructor(
    private val mainRepository: MainRepository
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: STARTING_PAGE_INDEX

        if (position>3){
            return LoadResult.Page(emptyList(), prevKey = position-1, nextKey = null)
        }
        return try {
            val response = mainRepository.getRecentPhotos(position)
            if (!response.isSuccessful){
                return LoadResult.Page(emptyList(), prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1, nextKey = null)
            }
            val photos = response.body()?.photos?.photo
            if (photos!=null) {
                LoadResult.Page(
                    data = photos,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (photos.isEmpty()) null else position + 1,
                )
            }else {
                return LoadResult.Page(emptyList(), prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1, nextKey = null)
            }

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
