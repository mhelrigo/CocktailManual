package mhelrigo.cocktailmanual.domain.usecase.drink

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.model.Drink
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchDrinkByNameUseCase @Inject constructor(private val drinkRepository: DrinkRepository) :
    UseCase<Flow<List<Drink>>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): Flow<List<Drink>> {
        return drinkRepository.searchByName(params?.get(0)!! as String)
    }
}