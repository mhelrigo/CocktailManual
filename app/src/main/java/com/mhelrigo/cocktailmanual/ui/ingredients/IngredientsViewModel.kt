package com.mhelrigo.cocktailmanual.ui.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mhelrigo.cocktailmanual.domain.model.Ingredient
import mhelrigo.cocktailmanual.domain.model.Ingredients
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.ingredients.GetAllIngredientUseCase
import mhelrigo.cocktailmanual.domain.usecase.ingredients.GetIngredientDetailUseCase
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    var getAllIngredientUseCase: GetAllIngredientUseCase,
    var getIngredientDetailUseCase: GetIngredientDetailUseCase,
    @Named("Dispatchers.IO") var mainCoroutineContext: CoroutineContext
) : ViewModel(), CoroutineScope {
    private val job = Job()

    private val _ingredients = MutableLiveData<ResultWrapper<Exception, Ingredients>>()
    val ingredients: LiveData<ResultWrapper<Exception, Ingredients>> get() = _ingredients

    private val _ingredient = MutableLiveData<ResultWrapper<Exception, Ingredient>>()
    val ingredient: LiveData<ResultWrapper<Exception, Ingredient>> get() = _ingredient

    fun requestForIngredients() = launch {
        _ingredients.postValue(ResultWrapper.buildLoading())
        when (val result = getAllIngredientUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _ingredients.postValue(ResultWrapper.build { result.value })
            }
            is ResultWrapper.Error -> {
                FirebaseCrashlytics.getInstance().recordException(result.error)
                _ingredients.postValue(ResultWrapper.build { throw Exception(result.error) })
            }
        }
    }

    fun requestForIngredient(p0: String) = launch {
        _ingredient.postValue(ResultWrapper.buildLoading())
        when (val result = getIngredientDetailUseCase.buildExecutable(listOf(p0))) {
            is ResultWrapper.Success -> {
                _ingredient.postValue(ResultWrapper.build { result.value.ingredients[0] })
            }
            is ResultWrapper.Error -> {
                _ingredient.postValue(ResultWrapper.build { throw Exception(result.error) })
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = mainCoroutineContext + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}