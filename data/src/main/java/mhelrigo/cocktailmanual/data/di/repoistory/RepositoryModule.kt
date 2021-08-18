package mhelrigo.cocktailmanual.data.di.repoistory

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mhelrigo.cocktailmanual.data.repository.drink.DrinkRepositoryImpl
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideDrinkRepository(drinkRepositoryImpl: DrinkRepositoryImpl): DrinkRepository
}