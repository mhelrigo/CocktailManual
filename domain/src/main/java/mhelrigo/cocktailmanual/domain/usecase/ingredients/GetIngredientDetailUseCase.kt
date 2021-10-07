package mhelrigo.cocktailmanual.domain.usecase.ingredients

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.entity.IngredientDetailEntity
import mhelrigo.cocktailmanual.domain.entity.IngredientsEntity
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetIngredientDetailUseCase @Inject constructor(private val ingredientRepository: IngredientRepository) :
    UseCase<Flow<IngredientDetailEntity>, GetIngredientDetailUseCase.Params>() {
    override suspend fun buildExecutable(params: Params): Flow<IngredientDetailEntity> {
        return ingredientRepository.getDetails(params.p0)
    }

    class Params(val p0: String)
}