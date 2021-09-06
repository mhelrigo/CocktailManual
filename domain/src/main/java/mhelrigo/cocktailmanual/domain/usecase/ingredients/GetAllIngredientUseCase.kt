package mhelrigo.cocktailmanual.domain.usecase.ingredients

import mhelrigo.cocktailmanual.domain.model.Ingredients
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllIngredientUseCase @Inject constructor(private val ingredientRepository: IngredientRepository) :
    UseCase<ResultWrapper<Exception, Ingredients>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Exception, Ingredients> {
        return ingredientRepository.getAll()
    }
}