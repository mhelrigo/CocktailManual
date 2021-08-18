package mhelrigo.cocktailmanual.domain.usecase.category

import mhelrigo.cocktailmanual.domain.model.Categories
import mhelrigo.cocktailmanual.domain.repository.CategoryRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase

class GetAllCategoryUseCase(private val categoryRepository: CategoryRepository) :
    UseCase<ResultWrapper<Categories, Exception>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Categories, Exception> {
        return categoryRepository.getAll()
    }
}