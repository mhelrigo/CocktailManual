package mhelrigo.cocktailmanual.domain.usecase.drink

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.entity.DrinkEntity
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLatestDrinksUseCase @Inject constructor(
    val drinkRepository: DrinkRepository
) :
    UseCase<Flow<List<DrinkEntity>>, GetLatestDrinksUseCase.Params>() {
    override suspend fun buildExecutable(params: Params): Flow<List<DrinkEntity>> {
        return drinkRepository.getLatest()
    }

    class Params()
}