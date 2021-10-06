package mhelrigo.cocktailmanual.data.repository.glass

import com.mhelrigo.commons.DISPATCHERS_IO
import kotlinx.coroutines.withContext
import mhelrigo.cocktailmanual.data.repository.glass.remote.GlassApi
import mhelrigo.cocktailmanual.domain.model.Glasses
import mhelrigo.cocktailmanual.domain.repository.GlassRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class GlassRepositoryImpl constructor(
    var glassApi: GlassApi,
    @Named(DISPATCHERS_IO) var ioCoroutineContext: CoroutineContext
) : GlassRepository {
    override suspend fun getAll(): ResultWrapper<Exception, Glasses> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { glassApi.getAll() }
        }
}