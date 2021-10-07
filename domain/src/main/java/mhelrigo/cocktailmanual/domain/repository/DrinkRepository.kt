package mhelrigo.cocktailmanual.domain.repository

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.entity.DrinkEntity

open interface DrinkRepository {
    suspend fun getRandomly(): Flow<List<DrinkEntity>>
    suspend fun getByPopularity(): Flow<List<DrinkEntity>>
    suspend fun getLatest(): Flow<List<DrinkEntity>>
    suspend fun searchByName(name: String): Flow<List<DrinkEntity>>
    suspend fun searchByIngredient(ingredient: String): Flow<List<DrinkEntity>>
    suspend fun getDetails(id: String): Flow<List<DrinkEntity>>
    suspend fun addFavoriteById(drinkEntity: DrinkEntity)
    suspend fun removeFavoriteById(drinkEntity: DrinkEntity)
    suspend fun selectAllFavorite(): Flow<List<DrinkEntity>>
}