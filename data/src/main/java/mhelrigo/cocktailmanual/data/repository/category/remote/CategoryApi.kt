package mhelrigo.cocktailmanual.data.repository.category.remote

import mhelrigo.cocktailmanual.domain.model.Categories
import retrofit2.http.GET

interface CategoryApi {
    @GET("list.php?c=list")
    suspend fun getAll(): Categories
}