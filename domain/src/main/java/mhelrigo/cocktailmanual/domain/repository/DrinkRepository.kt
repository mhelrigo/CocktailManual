package mhelrigo.cocktailmanual.domain.repository

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.model.Drink
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

interface DrinkRepository {
    suspend fun getRandomly(): Flow<List<Drink>>
    suspend fun getByPopularity(): Flow<List<Drink>>
    suspend fun getLatest(): Flow<List<Drink>>
    suspend fun searchByFirstLetter(firstLetter: String): ResultWrapper<Exception, Drinks>
    suspend fun searchByName(name: String): Flow<List<Drink>>
    suspend fun searchByIngredient(ingredient: String): Flow<List<Drink>>
    suspend fun searchByDrinkType(drinkType: String): ResultWrapper<Exception, Drinks>
    suspend fun searchByCategory(category: String): ResultWrapper<Exception, Drinks>
    suspend fun searchByGlass(glass: String): ResultWrapper<Exception, Drinks>
    suspend fun getDetails(id: String): Flow<List<Drink>>
    suspend fun addFavoriteById(drink: Drink)
    suspend fun removeFavoriteById(drink: Drink)
    suspend fun selectAllFavorite(): Flow<List<Drink>>
}