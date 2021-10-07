package mhelrigo.cocktailmanual.data.repository.ingredient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mhelrigo.cocktailmanual.data.mapper.IngredientMapper
import mhelrigo.cocktailmanual.data.repository.ingredient.remote.IngredientApi
import mhelrigo.cocktailmanual.domain.entity.IngredientDetailEntity
import mhelrigo.cocktailmanual.domain.entity.IngredientsEntity
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientRepositoryImpl @Inject constructor(
    var ingredientApi: IngredientApi,
    var ingredientMapper: IngredientMapper
) : IngredientRepository {
    override suspend fun getAll(): Flow<IngredientsEntity> = flow {
        emit(ingredientApi.getAll())
    }.map {
        ingredientMapper.transformList(it)
    }

    override suspend fun getDetails(name: String): Flow<IngredientDetailEntity> = flow {
        emit(ingredientApi.getDetails(name))
    }.map {
        ingredientMapper.transform(it)
    }
}