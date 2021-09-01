package mhelrigo.cocktailmanual.data.repository.drink.remote

import mhelrigo.cocktailmanual.domain.model.Drinks
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinkApi {
    @GET("randomselection.php")
    suspend fun getRandomly(): Drinks

    @GET("popular.php")
    suspend fun getByPopularity(): Drinks

    @GET("latest.php")
    suspend fun getLatest(): Drinks

    @GET("search.php")
    suspend fun searchByFirstLetter(@Query("f") s: String): Drinks

    @GET("search.php")
    suspend fun searchByName(@Query("s") s: String): Drinks

    @GET("filter.php")
    suspend fun searchByIngredient(@Query("i") s: String): Drinks

    @GET("filter.php")
    suspend fun searchByDrinkType(@Query("a") s: String): Drinks

    @GET("filter.php")
    suspend fun searchByCategory(@Query("c") s: String): Drinks

    @GET("filter.php")
    suspend fun searchByGlass(@Query("g") s: String): Drinks

    @GET("lookup.php")
    suspend fun getDetails(@Query("i") s: String): Drinks
}