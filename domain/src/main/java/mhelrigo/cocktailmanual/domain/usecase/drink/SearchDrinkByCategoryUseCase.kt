package mhelrigo.cocktailmanual.domain.usecase.drink

import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase

class SearchDrinkByCategoryUseCase(private val drinkRepository: DrinkRepository) :
    UseCase<ResultWrapper<Drinks, Exception>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Drinks, Exception> {
        return drinkRepository.searchByCategory(params?.get(0)!! as String)
    }
}