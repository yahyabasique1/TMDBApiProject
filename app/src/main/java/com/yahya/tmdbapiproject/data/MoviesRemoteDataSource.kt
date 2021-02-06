package com.yahya.tmdbapiproject.data


import com.yahya.tmdbapiproject.repository.network.*
import javax.inject.Inject
const val API_KEY="Enter your key here"
/* Works with the TMDB API to get data. */
class MoviesRemoteDataSource @Inject constructor(private val service: ApiInterface) : BaseDataSource() {

    suspend fun fetchMoviesList(page: Int, SORT_BY: String) : ResponseResult<PopularMoviesResponse> {
        return getResult { service.getMoviesList(API_KEY, page,sortBy = SORT_BY) }
    }

//    suspend fun fetchMovieImage(movieId: Long) : ResponseResult<MovieImagesResponse> {
//        return getResult { service.getMovieImages(API_KEY,movieId =  movieId) }
//    }
    suspend fun fetchMovieImage(movieId: Long) =
         getResult { service.getMovieImages(apiKey = API_KEY,movieId =  movieId) }

}
