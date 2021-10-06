package mhelrigo.cocktailmanual.domain.usecase.ingredients

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.model.Ingredients
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetIngredientDetailUseCase @Inject constructor(private val ingredientRepository: IngredientRepository) :
    UseCase<Flow<Ingredients>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): Flow<Ingredients> {
        return ingredientRepository.getDetails(params?.get(0)!! as String)
    }
}