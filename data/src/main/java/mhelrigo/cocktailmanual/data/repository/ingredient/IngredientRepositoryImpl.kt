package mhelrigo.cocktailmanual.data.repository.ingredient

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
    @Named("Dispatchers.IO") var ioCoroutineContext: CoroutineContext
) : IngredientRepository {
    override suspend fun getAll(): ResultWrapper<Exception, Ingredients> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { ingredientApi.getAll() }
        }

    override suspend fun getDetails(name: String): ResultWrapper<Exception, Ingredients> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { ingredientApi.getDetails(name) }
        }
}