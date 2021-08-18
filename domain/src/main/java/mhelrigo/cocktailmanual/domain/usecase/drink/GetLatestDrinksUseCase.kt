package mhelrigo.cocktailmanual.domain.usecase.drink

import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLatestDrinksUseCase @Inject constructor(var drinkRepository: DrinkRepository) : UseCase<ResultWrapper<Drinks, Exception>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Drinks, Exception> {
        return drinkRepository.getLatest()
    }
}