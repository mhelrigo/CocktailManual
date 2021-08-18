package mhelrigo.cocktailmanual.data.repository.glass.remote

import mhelrigo.cocktailmanual.domain.model.Glasses
import retrofit2.http.GET

interface GlassApi {
    @GET("list.php?g=list")
    suspend fun getAll(): Glasses
}