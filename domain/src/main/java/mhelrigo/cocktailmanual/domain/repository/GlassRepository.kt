package mhelrigo.cocktailmanual.domain.repository

import mhelrigo.cocktailmanual.domain.model.Glasses
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper

interface GlassRepository {
    suspend fun getAll() : ResultWrapper<Exception, Glasses>
}