package mhelrigo.cocktailmanual.domain.usecase.base

sealed class ResultWrapper<out A, out B> {
    data class Success<out A>(val value: A) : ResultWrapper<A, Nothing>()
    data class Error<out B>(val error: B) : ResultWrapper<Nothing, B>()

    companion object Factory {
        inline fun <V> build(function: () -> V): ResultWrapper<V, Exception> =
            try {
                Success(function.invoke())
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Error(e)
            }
    }
}