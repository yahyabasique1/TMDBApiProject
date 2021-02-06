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

/*

 */



@ExperimentalPagingApi
class MoviesFeedPagingSource(val moviesRemoteDataSource: MoviesRemoteDataSource, val appDatabase: AppDatabase, val SORT_BY: String) : PagingSource<Int, MoviesData>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesData> {

        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = moviesRemoteDataSource.fetchMoviesList(page,SORT_BY)
            Log.e("RESP","$response status ${response.status}")

            when(response.status){
                    ResponseResult.Status.SUCCESS ->{
                        appDatabase.getMoviesResultDao().insertAll(response.data?.moviesData!!)
                        LoadResult.Page(
                            response.data?.moviesData!!,
                            prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                            nextKey = if (response.data!!.moviesData.isEmpty()) null else page + 1
                        )
                    }

                ResponseResult.Status.ERROR ->{
                    LoadResult.Error(throwable = Throwable(response.message))
                }
                else -> {
                    LoadResult.Error(throwable = Throwable(response.message+""))

                }
            }

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }


}