package mhelrigo.cocktailmanual.domain.usecase.drink

import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoveFavoriteUseCase @Inject constructor(val drinkRepository: DrinkRepository) :
    UseCase<ResultWrapper<Exception, Unit>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Exception, Unit>? {
        val id = Integer.parseInt(params?.get(0) as String)
        return drinkRepository.removeFavoriteById(id)
    }
}