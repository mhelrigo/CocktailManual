package mhelrigo.cocktailmanual.domain.usecase.base

abstract class UseCase<T, in Params>() where Params : List<Any> {
    abstract suspend fun buildExecutable(params: Params? = null): T?
}