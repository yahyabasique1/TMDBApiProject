package com.yahya.tmdbapiproject.view.moviesfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.yahya.tmdbapiproject.data.MoviesRepository
import com.yahya.tmdbapiproject.repository.network.MoviesData
import javax.inject.Inject

@ExperimentalPagingApi
class MoviesFeedVM @Inject constructor(var moviesRepository: MoviesRepository) : ViewModel() {

    @ExperimentalPagingApi
    suspend fun fetchMoviesList(
        isInter: Boolean,
        SORT_BY: String
    ): LiveData<PagingData<MoviesData>> =
        moviesRepository.fetchMovies(isInter, SORT_BY)


}