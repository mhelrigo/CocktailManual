package mhelrigo.cocktailmanual.data.di.repoistory

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mhelrigo.cocktailmanual.data.repository.drink.DrinkRepositoryImpl
import mhelrigo.cocktailmanual.data.repository.ingredient.IngredientRepositoryImpl
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideDrinkRepository(drinkRepositoryImpl: DrinkRepositoryImpl): DrinkRepository

    @Binds
    abstract fun provideIngredientsRepository(ingredientRepositoryImpl: IngredientRepositoryImpl): IngredientRepository
}