package mhelrigo.cocktailmanual.data.repository.ingredient.remote

import mhelrigo.cocktailmanual.data.entity.ingredient.IngredientsApiEntity
import mhelrigo.cocktailmanual.domain.entity.IngredientsEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface IngredientApi {
    @GET("list.php?i=list")
    suspend fun getAll() : IngredientsApiEntity

    @GET("search.php")
    suspend fun getDetails(@Query("i") name: String) : IngredientsApiEntity
}