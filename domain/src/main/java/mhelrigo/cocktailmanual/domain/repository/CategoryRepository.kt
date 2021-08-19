package mhelrigo.cocktailmanual.domain.repository

import mhelrigo.cocktailmanual.domain.model.Categories
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

interface CategoryRepository {
    suspend fun getAll() : ResultWrapper<Exception, Categories>
}