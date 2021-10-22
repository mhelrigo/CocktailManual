package mhelrigo.cocktailmanual.data.di.local

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mhelrigo.cocktailmanual.data.repository.CocktailDatabase
import mhelrigo.cocktailmanual.data.repository.drink.local.DrinkDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {
    @Provides
    @Singleton
    fun provideCocktailDatabase(@ApplicationContext context: Context): CocktailDatabase =
        Room.databaseBuilder(context, CocktailDatabase::class.java, CocktailDatabase.name).build()

    @Provides
    @Singleton
    fun provideDrinkDao(cocktailDatabase: CocktailDatabase): DrinkDao = cocktailDatabase.drinkDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            "com.mhelrigo.cocktailmanual.sharedPreferences",
            Context.MODE_PRIVATE
        )
}