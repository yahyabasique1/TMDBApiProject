package com.yahya.tmdbapiproject

import android.app.Activity
import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.yahya.com.yahya.tmdbapiproject.di.component.ApplicationComponent
import com.yahya.com.yahya.tmdbapiproject.di.component.DaggerApplicationComponent
import com.yahya.tmdbapiproject.di.module.ApplicationModule
import com.yahya.tmdbapiproject.di.module.DataBaseModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

@ExperimentalPagingApi
class MoviesApplication: Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    protected lateinit var mApplicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        mApplicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .dataBaseModule(DataBaseModule(this))
            .build()
        mApplicationComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

}