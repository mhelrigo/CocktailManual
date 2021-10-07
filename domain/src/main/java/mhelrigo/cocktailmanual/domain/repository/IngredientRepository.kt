package mhelrigo.cocktailmanual.domain.repository

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.entity.IngredientDetailEntity
import mhelrigo.cocktailmanual.domain.entity.IngredientsEntity

interface IngredientRepository {
    suspend fun getAll() : Flow<IngredientsEntity>
    suspend fun getDetails(name: String) : Flow<IngredientDetailEntity>
}