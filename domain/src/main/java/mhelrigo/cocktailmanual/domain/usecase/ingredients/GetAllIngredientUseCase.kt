package mhelrigo.cocktailmanual.domain.usecase.ingredients

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.entity.IngredientsEntity
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllIngredientUseCase @Inject constructor(private val ingredientRepository: IngredientRepository) :
    UseCase<Flow<IngredientsEntity>, GetAllIngredientUseCase.Params>() {
    override suspend fun buildExecutable(params: Params): Flow<IngredientsEntity> {
        return ingredientRepository.getAll()
    }

    class Params()
}