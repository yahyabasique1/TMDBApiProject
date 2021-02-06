package com.yahya.tmdbapiproject.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.yahya.tmdbapiproject.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private var application: Application) {

    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(): Context {

        return application

    }

    @Singleton
    @Provides
    fun applicationContext(): Application {
        return application
    }

    @Singleton
    @Provides
    fun sharedPreference(): SharedPreferences {
        return application.getSharedPreferences("moviesfeedpref", Context.MODE_PRIVATE)
    }

}