package mhelrigo.cocktailmanual.domain.usecase.glass

import mhelrigo.cocktailmanual.domain.model.Glasses
import mhelrigo.cocktailmanual.domain.repository.GlassRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase

class GetAllGlassUseCase(private val glassRepository: GlassRepository) :
    UseCase<ResultWrapper<Glasses, Exception>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Glasses, Exception> {
        return glassRepository.getAll()
    }
}