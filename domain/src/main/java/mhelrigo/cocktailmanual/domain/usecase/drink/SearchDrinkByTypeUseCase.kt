package mhelrigo.cocktailmanual.domain.usecase.drink

import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase

class SearchDrinkByTypeUseCase(private val drinkRepository: DrinkRepository) :
    UseCase<ResultWrapper<Exception, Drinks>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Exception, Drinks> {
        return drinkRepository.searchByDrinkType(params?.get(0)!! as String)
    }
}