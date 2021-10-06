package mhelrigo.cocktailmanual.data.repository.ingredient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import mhelrigo.cocktailmanual.data.repository.ingredient.remote.IngredientApi
import mhelrigo.cocktailmanual.domain.model.Ingredients
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class IngredientRepositoryImpl @Inject constructor(
    var ingredientApi: IngredientApi,
) : IngredientRepository {
    override suspend fun getAll(): Flow<Ingredients> = flow {
        emit(ingredientApi.getAll())
    }

    override suspend fun getDetails(name: String): Flow<Ingredients> = flow {
        emit(ingredientApi.getDetails(name))
    }
}