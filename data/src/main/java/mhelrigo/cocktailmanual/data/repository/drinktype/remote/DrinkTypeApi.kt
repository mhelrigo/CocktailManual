package mhelrigo.cocktailmanual.data.repository.drinktype.remote

import mhelrigo.cocktailmanual.domain.model.DrinkTypes
import retrofit2.http.GET

interface DrinkTypeApi {
    @GET("list.php?a=list")
    suspend fun getAll() : DrinkTypes
}