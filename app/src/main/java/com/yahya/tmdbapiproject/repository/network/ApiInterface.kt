package com.yahya.tmdbapiproject.repository.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiInterface {
    @GET("/3/movie/popular")
    suspend fun getPopularMoviesList(

        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String = "en"
    ): Response<PopularMoviesResponse>

    @GET("/3/discover/movie")
    suspend fun getMoviesList(

            @Query("api_key") apiKey: String? = null,
            @Query("page") page: Int? = null,
            @Query("language") language: String = "en",
            @Query("sort_by") sortBy:String="popularity.asc"
    ): Response<PopularMoviesResponse>

    @GET("/3/movie/{movie_id}/images")
    suspend fun getMovieImages(
            @Path("movie_id") movieId:Long,
            @Query("api_key") apiKey: String? = null,
            @Query("language") language: String = "en",

    ): Response<MovieImagesResponse>

    companion object {
        const val URL_ENDPOINT =
                "https://api.themoviedb.org"
    }
}