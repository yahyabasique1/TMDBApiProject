package com.yahya.tmdbapiproject.di.module

import android.content.Context
import androidx.room.Room
import com.yahya.tmdbapiproject.di.DatabaseInfo
import com.yahya.tmdbapiproject.repository.local.AppDatabase
import com.yahya.tmdbapiproject.repository.local.MoviesDataDao
import com.yahya.tmdbapiproject.repository.local.RemoteKeysDao
import com.yahya.tmdbapiproject.repository.network.MoviesData
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule(private var context: Context) {


    @DatabaseInfo
    var databaseName = "tmdbmovies.db"


    @Provides
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName
        ).allowMainThreadQueries()
            .build()
    }

    @Provides
    @DatabaseInfo
    fun ovideDatabseName(): String {
        return databaseName
    }


    @Singleton
    @Provides
    fun provideRemoteDao(appDatabase: AppDatabase): RemoteKeysDao {
        return appDatabase.getRepoDao()
    }

    @Singleton
    @Provides
    fun provideMoviesDao(appDatabase: AppDatabase): MoviesDataDao {
        return appDatabase.getMoviesResultDao()
    }


}