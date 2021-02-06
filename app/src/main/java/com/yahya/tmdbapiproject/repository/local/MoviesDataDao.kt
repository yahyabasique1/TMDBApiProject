package com.yahya.tmdbapiproject.repository.local

import androidx.paging.PagingSource
import androidx.room.*
import com.yahya.tmdbapiproject.repository.network.MoviesData

@Dao
interface MoviesDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(moviesDataList: List<MoviesData>)

    @Query("SELECT * FROM moviesdata")
    fun getAllMovies(): PagingSource<Int, MoviesData>

    @Query("SELECT * FROM moviesdata")
    fun getAllMoviesList():List<MoviesData>

    @Query("DELETE FROM moviesdata")
    suspend fun clearAllMovies()

}