package mhelrigo.cocktailmanual.data.repository.ingredient.remote

import mhelrigo.cocktailmanual.domain.model.Ingredients
import retrofit2.http.GET
import retrofit2.http.Query

interface IngredientApi {
    @GET("list.php?i=list")
    suspend fun getAll() : Ingredients

    @GET("search.php")
    suspend fun getDetails(@Query("i") name: String) : Ingredients
}