package mhelrigo.cocktailmanual.domain.repository

import mhelrigo.cocktailmanual.domain.model.Drink
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

interface DrinkRepository {
    suspend fun getRandomly(): ResultWrapper<Exception, Drinks>
    suspend fun getByPopularity(): ResultWrapper<Exception, Drinks>
    suspend fun getLatest(): ResultWrapper<Exception, Drinks>
    suspend fun searchByFirstLetter(firstLetter: String): ResultWrapper<Exception, Drinks>
    suspend fun searchByName(name: String): ResultWrapper<Exception, Drinks>
    suspend fun searchByIngredient(ingredient: String): ResultWrapper<Exception, Drinks>
    suspend fun searchByDrinkType(drinkType: String): ResultWrapper<Exception, Drinks>
    suspend fun searchByCategory(category: String): ResultWrapper<Exception, Drinks>
    suspend fun searchByGlass(glass: String): ResultWrapper<Exception, Drinks>
    suspend fun getDetails(id: String): ResultWrapper<Exception, Drinks>
    suspend fun addFavoriteById(id: Int): ResultWrapper<Exception, Unit>
    suspend fun removeFavoriteById(id: Int): ResultWrapper<Exception, Unit>
    suspend fun selectFavoriteById(id: String): ResultWrapper<Exception, Drink>
    suspend fun selectAllFavorite(): ResultWrapper<Exception, List<Drink>>
}