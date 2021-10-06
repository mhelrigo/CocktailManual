package mhelrigo.cocktailmanual.domain.repository

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.model.Ingredients

interface IngredientRepository {
    suspend fun getAll() : Flow<Ingredients>
    suspend fun getDetails(name: String) : Flow<Ingredients>
}