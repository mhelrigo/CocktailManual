package mhelrigo.cocktailmanual.domain.usecase.drinktype

import mhelrigo.cocktailmanual.domain.model.DrinkTypes
import mhelrigo.cocktailmanual.domain.repository.DrinkTypeRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase

class GetAllDrinkTypeUseCase(private val drinkTypeRepository: DrinkTypeRepository) :
    UseCase<ResultWrapper<Exception, DrinkTypes>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Exception, DrinkTypes> {
        return drinkTypeRepository.getAll()
    }
}