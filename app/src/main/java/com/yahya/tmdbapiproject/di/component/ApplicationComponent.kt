package com.yahya.com.yahya.tmdbapiproject.di.component

import android.app.Application
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.yahya.tmdbapiproject.MoviesApplication
import com.yahya.tmdbapiproject.di.ApplicationContext
import com.yahya.tmdbapiproject.di.module.ActivityModule
import com.yahya.tmdbapiproject.di.module.ApiInterfaceModule
import com.yahya.tmdbapiproject.di.module.ApplicationModule
import com.yahya.tmdbapiproject.di.module.DataBaseModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@ExperimentalPagingApi
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        DataBaseModule::class,
        ActivityModule::class,
        ApiInterfaceModule::class
    ]
)
interface ApplicationComponent {


    fun inject(testApplication: MoviesApplication)


    @ApplicationContext
    fun getContext(): Context

    fun application(): Application

//    fun apiInterface(): ApiInterface


}