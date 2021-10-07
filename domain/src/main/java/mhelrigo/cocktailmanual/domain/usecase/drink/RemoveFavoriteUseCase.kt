package mhelrigo.cocktailmanual.domain.usecase.drink

import mhelrigo.cocktailmanual.domain.entity.DrinkEntity
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoveFavoriteUseCase @Inject constructor(val drinkRepository: DrinkRepository) :
    UseCase<Unit, RemoveFavoriteUseCase.Params>() {
    override suspend fun buildExecutable(params: Params) {
        return drinkRepository.removeFavoriteById(params.p0)
    }

    class Params(val p0: DrinkEntity)
}