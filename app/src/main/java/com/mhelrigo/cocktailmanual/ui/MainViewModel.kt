package com.mhelrigo.cocktailmanual.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.drink.GetLatestDrinksUseCase
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel @Inject constructor(
    var drinksUseCase: GetLatestDrinksUseCase,
    @Named("Dispatchers.Main") var uiCoroutineContext: CoroutineContext
) : ViewModel(), CoroutineScope {
    fun fetchAMeal() = launch {
        when (val a: ResultWrapper<Drinks, Exception> = drinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> Timber.e("Success: ${a.value}")
            is ResultWrapper.Error -> Timber.e("Error: ${a.error}")
        }
    }

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = uiCoroutineContext + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}