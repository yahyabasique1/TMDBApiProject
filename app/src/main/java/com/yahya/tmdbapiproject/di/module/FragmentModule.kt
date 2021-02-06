package com.yahya.tmdbapiproject.di.module

import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import com.yahya.tmdbapiproject.di.FragmentScope
import com.yahya.tmdbapiproject.view.moviedetail.MovieDetail
import com.yahya.tmdbapiproject.view.moviesfeed.MoviesFeedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@ExperimentalPagingApi
@Module
abstract class FragmentModule {

    @FragmentScope(Fragment::class)
    @ContributesAndroidInjector
    abstract fun getMoviesFeedFragment(): MoviesFeedFragment

    @FragmentScope(Fragment::class)
    @ContributesAndroidInjector
    abstract fun getMoviesDetail(): MovieDetail

}