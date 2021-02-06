package com.yahya.tmdbapiproject.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.yahya.tmdbapiproject.view.moviedetail.MovoeDetailVM
import com.yahya.tmdbapiproject.view.moviesfeed.MoviesFeedVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@ExperimentalPagingApi
@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MoviesFeedVM::class)
    abstract fun bindThemeViewModel(viewModel: MoviesFeedVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovoeDetailVM::class)
    abstract fun bindThemeMovoeDetailVM(viewModel: MovoeDetailVM): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
