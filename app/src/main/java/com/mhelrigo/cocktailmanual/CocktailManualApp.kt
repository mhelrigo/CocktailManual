package com.mhelrigo.cocktailmanual

import android.app.Application
import com.squareup.picasso.Picasso
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CocktailManualApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Picasso.get().setIndicatorsEnabled(true)
    }
}