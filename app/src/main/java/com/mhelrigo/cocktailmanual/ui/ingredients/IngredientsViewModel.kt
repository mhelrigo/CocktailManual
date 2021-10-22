package com.mhelrigo.cocktailmanual.ui.ingredients

import androidx.lifecycle.ViewModel
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import com.mhelrigo.commons.DISPATCHERS_IO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mhelrigo.cocktailmanual.domain.entity.IngredientEntity
import mhelrigo.cocktailmanual.domain.entity.IngredientsEntity
import mhelrigo.cocktailmanual.domain.usecase.ingredients.GetAllIngredientUseCase
import mhelrigo.cocktailmanual.domain.usecase.ingredients.GetIngredientDetailUseCase
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    var getAllIngredientUseCase: GetAllIngredientUseCase,
    var getIngredientDetailUseCase: GetIngredientDetailUseCase,
    @Named(DISPATCHERS_IO) var mainCoroutineContext: CoroutineContext
) : ViewModel(), CoroutineScope {
    private val job = Job()

    private val _ingredients =
        MutableStateFlow<ViewStateWrapper<IngredientsEntity>>(ViewStateWrapper.Init)
    val ingredients: StateFlow<ViewStateWrapper<IngredientsEntity>> get() = _ingredients

    private val _ingredient =
        MutableStateFlow<ViewStateWrapper<IngredientEntity>>(ViewStateWrapper.Init)
    val ingredient: StateFlow<ViewStateWrapper<IngredientEntity>> get() = _ingredient


    fun requestForIngredients() = launch {
        getAllIngredientUseCase.buildExecutable(GetAllIngredientUseCase.Params())
            .onStart {
                _ingredients.value = ViewStateWrapper.Loading(0)
            }.catch { throwable ->
                _ingredients.value = ViewStateWrapper.Error(throwable)
            }.collect { data ->
                _ingredients.value = ViewStateWrapper.Success(data)
            }
    }

    fun requestForIngredient(p0: String) = launch {
        getIngredientDetailUseCase.buildExecutable(GetIngredientDetailUseCase.Params(p0))
            .onStart {
                _ingredient.value = ViewStateWrapper.Loading(0)
            }.catch { throwable ->
                _ingredient.value = ViewStateWrapper.Error(throwable)
            }.collect { data ->
                _ingredient.value = ViewStateWrapper.Success(data.ingredient[0])
            }
    }

    override val coroutineContext: CoroutineContext
        get() = mainCoroutineContext + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}