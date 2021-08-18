package com.mhelrigo.cocktailmanual.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    @Named("Dispatchers.IO")
    fun provideIOCoroutineContext() : CoroutineContext = Dispatchers.IO

    @Singleton
    @Provides
    @Named("Dispatchers.Main")
    fun provideUICoroutineContext() : CoroutineContext = Dispatchers.Main
}