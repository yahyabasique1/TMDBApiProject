package com.yahya.tmdbapiproject.view.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.yahya.tmdbapiproject.data.MoviesRepository
import com.yahya.tmdbapiproject.repository.network.MovieImagesResponse
import javax.inject.Inject

@ExperimentalPagingApi
class MovoeDetailVM @Inject constructor(val repository: MoviesRepository):ViewModel() {
    val mutableLiveData=MutableLiveData<MovieImagesResponse>()
    suspend fun getMovieImages(moviedId:Long):LiveData<MovieImagesResponse>{
       return repository.getMovieImages(moviedId)
    }

}