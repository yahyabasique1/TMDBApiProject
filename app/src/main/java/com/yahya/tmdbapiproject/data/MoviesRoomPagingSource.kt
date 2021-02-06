package com.yahya.tmdbapiproject.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.yahya.tmdbapiproject.data.MoviesRepository.Companion.DEFAULT_PAGE_INDEX
import com.yahya.tmdbapiproject.repository.local.AppDatabase
import com.yahya.tmdbapiproject.repository.network.MoviesData
import com.yahya.tmdbapiproject.repository.network.ResponseResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class MoviesRoomPagingSource constructor(val appDatabase: AppDatabase) : PagingSource<Int, MoviesData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesData> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = appDatabase.getMoviesResultDao().getAllMoviesList()
            Log.e("RESP"," status ${response.size}")

            LoadResult.Page(
                    response,
                    prevKey = if (page == MoviesRepository.DEFAULT_PAGE_INDEX) null else page - 1,
                    nextKey = if (response.isEmpty()) null else page + 1
            )


        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}