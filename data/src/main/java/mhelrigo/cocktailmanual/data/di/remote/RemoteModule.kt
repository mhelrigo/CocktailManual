package mhelrigo.cocktailmanual.data.di.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mhelrigo.cocktailmanual.data.BuildConfig
import mhelrigo.cocktailmanual.data.repository.drink.remote.DrinkApi
import mhelrigo.cocktailmanual.data.repository.ingredient.remote.IngredientApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RemoteModule {
    @Singleton
    @Provides
    fun provideApiHandler(): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.thecocktaildb.com/api/json/v2/${BuildConfig.API_KEY}/").build()

    @Singleton
    @Provides
    fun provideDrinkApi(retrofit: Retrofit): DrinkApi = retrofit.create(DrinkApi::class.java)

    @Singleton
    @Provides
    fun provideIngredientApi(retrofit: Retrofit) : IngredientApi = retrofit.create(IngredientApi::class.java)
}