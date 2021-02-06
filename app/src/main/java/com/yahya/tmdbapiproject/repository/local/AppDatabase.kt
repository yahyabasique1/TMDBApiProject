package com.yahya.tmdbapiproject.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yahya.tmdbapiproject.repository.network.MoviesData

@Database(version = 1, entities = [MoviesData::class, RemoteKey::class], exportSchema = false)
abstract class AppDatabase: RoomDatabase()  {

    abstract fun getRepoDao(): RemoteKeysDao
    abstract fun getMoviesResultDao():MoviesDataDao

    companion object {

        val DB_NAME = "tmdbmovies.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                .build()
    }
}