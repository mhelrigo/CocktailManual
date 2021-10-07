package mhelrigo.cocktailmanual.domain.usecase.drink

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.entity.DrinkEntity
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchDrinkByIngredientsUseCase @Inject constructor(private val drinkRepository: DrinkRepository) :
    UseCase<Flow<List<DrinkEntity>>, SearchDrinkByIngredientsUseCase.Params>() {
    override suspend fun buildExecutable(params: Params): Flow<List<DrinkEntity>> {
        return drinkRepository.searchByIngredient(params.p0)
    }

    class Params(val p0: String)
}