package com.yahya.tmdbapiproject.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.paging.rxjava2.observable
import com.yahya.tmdbapiproject.repository.local.AppDatabase
import com.yahya.tmdbapiproject.repository.network.MovieImagesResponse
import com.yahya.tmdbapiproject.repository.network.MoviesData
import com.yahya.tmdbapiproject.repository.network.ResponseResult
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
class MoviesRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val appDatabase: AppDatabase
) {
    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 10

    }

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    /**
     * calling the paging source to give results from api calls
     * and returning the results in the form of flow [Flow<PagingData<MoviesData>>]
     * since the [PagingDataAdapter] accepts the [PagingData] as the source in later stage
     */
    fun letMoviesFeedFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<MoviesData>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { MoviesFeedPagingSource(remoteDataSource, appDatabase, "") }
        ).flow
    }

    //for rxjava
    fun letMoviesFeedObservable(pagingConfig: PagingConfig = getDefaultPageConfig()): Observable<PagingData<MoviesData>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { MoviesFeedPagingSource(remoteDataSource, appDatabase, "") }
        ).observable
    }

    //for live data
    fun letMoviesFeedLiveData(
        pagingConfig: PagingConfig = getDefaultPageConfig(),
        SORT_BY: String
    ): LiveData<PagingData<MoviesData>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { MoviesFeedPagingSource(remoteDataSource, appDatabase, SORT_BY) }
        ).liveData
    }

    fun fetchOfflineData(pagingConfig: PagingConfig = getDefaultPageConfig()): LiveData<PagingData<MoviesData>> {
        return fetchMoviesFeedLiveDataDb()
    }

    fun fetchMoviesFeedLiveDataDb(
        query: String = "Cristiano Ronaldo",
        pagingConfig: PagingConfig = getDefaultPageConfig()
    ): LiveData<PagingData<MoviesData>> {
        if (appDatabase == null) throw IllegalStateException("Database is not initialized")

        val pagingSourceFactory = { appDatabase.getMoviesResultDao().getAllMovies() }

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory
//                remoteMediator = MovieMediator(remoteDataSource,appDatabase)
        ).liveData
    }


    fun fetchMovies(
        isInternetAvailabile: Boolean,
        SORT_BY: String
    ): LiveData<PagingData<MoviesData>> {
        return if (isInternetAvailabile) {
            CoroutineScope(Dispatchers.Main).launch {
                appDatabase.getMoviesResultDao().clearAllMovies()
            }
            letMoviesFeedLiveData(pagingConfig = getDefaultPageConfig(), SORT_BY)
        } else {
            fetchOfflineData()
        }
    }

    suspend fun getMovieImages(movieId: Long): LiveData<MovieImagesResponse> {
        val mutableLiveData = MutableLiveData<MovieImagesResponse>()
        val response = remoteDataSource.fetchMovieImage(movieId)
        Log.e("RESPONSSS", "$response")
        when (response.status) {
            ResponseResult.Status.SUCCESS -> {
                mutableLiveData.postValue(response.data)
            }

            ResponseResult.Status.ERROR -> {
                mutableLiveData.postValue(null)

            }

        }

        return mutableLiveData

    }
}