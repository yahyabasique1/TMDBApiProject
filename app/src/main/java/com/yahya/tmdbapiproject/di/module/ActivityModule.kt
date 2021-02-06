package com.yahya.tmdbapiproject.di.module

import androidx.paging.ExperimentalPagingApi
import com.yahya.tmdbapiproject.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ExperimentalPagingApi
@Module(includes = [ViewModelModule::class])
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity


}