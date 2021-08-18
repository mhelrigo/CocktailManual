package mhelrigo.cocktailmanual.domain.usecase.ingredients

import mhelrigo.cocktailmanual.domain.model.Ingredients
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase

class GetAllIngredientUseCase(private val ingredientRepository: IngredientRepository) :
    UseCase<ResultWrapper<Ingredients, Exception>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Ingredients, Exception> {
        return ingredientRepository.getAll()
    }
}