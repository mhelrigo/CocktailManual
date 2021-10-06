package mhelrigo.cocktailmanual.domain.usecase.drink

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.model.Drink
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddFavoriteUseCase @Inject constructor(var drinkRepository: DrinkRepository) :
    UseCase<Unit, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?) {
        return drinkRepository.addFavoriteById(params?.get(0) as Drink)
    }
}