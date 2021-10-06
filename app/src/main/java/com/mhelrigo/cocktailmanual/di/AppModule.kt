package com.mhelrigo.cocktailmanual.di

import android.content.Context
import com.mhelrigo.cocktailmanual.R
import com.mhelrigo.commons.DISPATCHERS_IO
import com.mhelrigo.commons.DISPATCHERS_MAIN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    const val IS_TABLET = "IS_TABLET"

    @Singleton
    @Provides
    @Named(DISPATCHERS_IO)
    fun provideIOCoroutineContext(): CoroutineContext = Dispatchers.IO

    @Singleton
    @Provides
    @Named(DISPATCHERS_MAIN)
    fun provideUICoroutineContext(): CoroutineContext = Dispatchers.Main

    @Provides
    @Named(IS_TABLET)
    fun isTablet(@ApplicationContext context: Context): Boolean =
        context.resources.getBoolean(R.bool.isTablet)
}