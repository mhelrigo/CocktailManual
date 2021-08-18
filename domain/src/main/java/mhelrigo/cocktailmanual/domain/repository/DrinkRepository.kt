package mhelrigo.cocktailmanual.domain.repository

import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

interface DrinkRepository {
    suspend fun getRandomly(): ResultWrapper<Drinks, Exception>
    suspend fun getByPopularity(): ResultWrapper<Drinks, Exception>
    suspend fun getLatest(): ResultWrapper<Drinks, Exception>
    suspend fun searchByFirstLetter(firstLetter: String): ResultWrapper<Drinks, Exception>
    suspend fun searchByName(name: String): ResultWrapper<Drinks, Exception>
    suspend fun searchByIngredient(ingredient: String): ResultWrapper<Drinks, Exception>
    suspend fun searchByDrinkType(drinkType: String): ResultWrapper<Drinks, Exception>
    suspend fun searchByCategory(category: String): ResultWrapper<Drinks, Exception>
    suspend fun searchByGlass(glass: String): ResultWrapper<Drinks, Exception>
    suspend fun getDetails(id: String): ResultWrapper<Drinks, Exception>
    suspend fun addFavoriteById(id: String) : ResultWrapper<Unit, Exception>
    suspend fun removeFavoriteById(id: String): ResultWrapper<Unit, Exception>
}