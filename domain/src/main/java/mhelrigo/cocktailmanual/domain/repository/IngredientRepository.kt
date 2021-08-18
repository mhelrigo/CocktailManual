package mhelrigo.cocktailmanual.domain.repository

import mhelrigo.cocktailmanual.domain.model.Ingredients
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

interface IngredientRepository {
    suspend fun getAll() : ResultWrapper<Ingredients, Exception>
    suspend fun getDetails(name: String) : ResultWrapper<Ingredients, Exception>
}