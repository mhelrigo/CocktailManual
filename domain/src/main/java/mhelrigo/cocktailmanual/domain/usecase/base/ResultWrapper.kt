package mhelrigo.cocktailmanual.domain.usecase.base

sealed class ResultWrapper<out A, out B> {
    data class Success<out B>(val value: B) : ResultWrapper<Nothing, B>()
    object Loading : ResultWrapper<Nothing, Nothing>()
    data class Error<out A>(val error: A) : ResultWrapper<A, Nothing>()

    companion object Factory {
        inline fun <V> build(function: () -> V): ResultWrapper<Exception, V> =
            try {
                Success(function.invoke())
            } catch (e: Exception) {
                Error(e)
            }

        fun buildLoading(): ResultWrapper<Exception, Nothing> {
            return Loading
        }
    }
}