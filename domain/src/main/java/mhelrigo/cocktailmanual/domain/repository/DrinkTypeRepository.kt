package mhelrigo.cocktailmanual.domain.repository

import mhelrigo.cocktailmanual.domain.model.DrinkTypes
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

interface DrinkTypeRepository {
    suspend fun getAll(): ResultWrapper<Exception, DrinkTypes>
}