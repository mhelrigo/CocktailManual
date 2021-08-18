package com.mhelrigo.cocktailmanual.utils

sealed class ResultWrapper<out R> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Exception) : ResultWrapper<Nothing>()
    data class Loading<out T>(val data: T) : ResultWrapper<T>()
}