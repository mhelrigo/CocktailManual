package mhelrigo.cocktailmanual.domain.usecase.base

interface UseCaseResponse<T> {
    fun success(response: T)
    fun failed(response: Exception)
}