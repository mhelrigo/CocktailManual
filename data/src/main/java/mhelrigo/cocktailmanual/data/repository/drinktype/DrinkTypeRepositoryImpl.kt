package mhelrigo.cocktailmanual.data.repository.drinktype

import com.mhelrigo.commons.DISPATCHERS_IO
import kotlinx.coroutines.withContext
import mhelrigo.cocktailmanual.data.repository.drinktype.remote.DrinkTypeApi
import mhelrigo.cocktailmanual.domain.model.DrinkTypes
import mhelrigo.cocktailmanual.domain.repository.DrinkTypeRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class DrinkTypeRepositoryImpl constructor(
    var drinkTypeApi: DrinkTypeApi,
    @Named(DISPATCHERS_IO) var ioCoroutineContext: CoroutineContext
) :
    DrinkTypeRepository {
    override suspend fun getAll(): ResultWrapper<Exception, DrinkTypes> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkTypeApi.getAll() }
        }
}