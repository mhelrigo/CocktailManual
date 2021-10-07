package mhelrigo.cocktailmanual.data.repository.drink.remote

import mhelrigo.cocktailmanual.data.entity.drink.DrinksApiEntity
import mhelrigo.cocktailmanual.domain.entity.DrinksEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinkApi {
    @GET("randomselection.php")
    suspend fun getRandomly(): DrinksApiEntity

    @GET("popular.php")
    suspend fun getByPopularity(): DrinksApiEntity

    @GET("latest.php")
    suspend fun getLatest(): DrinksApiEntity

    @GET("search.php")
    suspend fun searchByName(@Query("s") s: String): DrinksApiEntity

    @GET("filter.php")
    suspend fun searchByIngredient(@Query("i") s: String): DrinksApiEntity

    @GET("lookup.php")
    suspend fun getDetails(@Query("i") s: String): DrinksApiEntity
}