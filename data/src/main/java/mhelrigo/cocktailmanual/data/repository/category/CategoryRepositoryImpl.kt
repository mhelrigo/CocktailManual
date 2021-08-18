package mhelrigo.cocktailmanual.data.repository.category

import kotlinx.coroutines.withContext
import mhelrigo.cocktailmanual.data.repository.category.remote.CategoryApi
import mhelrigo.cocktailmanual.domain.model.Categories
import mhelrigo.cocktailmanual.domain.repository.CategoryRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class CategoryRepositoryImpl constructor(
    var categoryApi: CategoryApi,
    @Named("Dispatchers.IO") var ioCoroutineContext: CoroutineContext
) : CategoryRepository {
    override suspend fun getAll(): ResultWrapper<Categories, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { categoryApi.getAll() }
        }
}