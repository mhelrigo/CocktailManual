package com.mhelrigo.cocktailmanual.ui.commons

sealed class ViewStateWrapper<out T> {
    object Init : ViewStateWrapper<Nothing>()
    data class Loading<T>(val progress: Long?) : ViewStateWrapper<T>()
    data class Success<T>(var data: T) : ViewStateWrapper<T>()
    data class Error<T>(var throwable: Throwable) : ViewStateWrapper<T>()
}