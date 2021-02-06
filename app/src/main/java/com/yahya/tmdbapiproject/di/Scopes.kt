package com.yahya.tmdbapiproject.di

import androidx.fragment.app.Fragment
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.reflect.KClass

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DatabaseInfo

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope(val value: KClass<out Fragment>)