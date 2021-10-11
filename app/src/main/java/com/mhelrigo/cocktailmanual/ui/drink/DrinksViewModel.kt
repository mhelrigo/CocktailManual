package com.mhelrigo.cocktailmanual.ui.drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhelrigo.cocktailmanual.mapper.DrinkModelMapper
import com.mhelrigo.cocktailmanual.model.DrinkModel
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import com.mhelrigo.cocktailmanual.model.DrinkModel.Factory.assignCollectionType
import com.mhelrigo.cocktailmanual.model.DrinkCollectionType
import com.mhelrigo.commons.DISPATCHERS_IO
import com.mhelrigo.commons.SEARCH_DELAY_IN_MILLIS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import mhelrigo.cocktailmanual.domain.usecase.drink.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class DrinksViewModel @Inject constructor(
    var popularDrinksUseCase: GetPopularDrinksUseCase,
    var latestDrinksUseCase: GetLatestDrinksUseCase,
    var randomDrinksUseCase: GetRandomDrinksUseCase,
    var addFavoriteUseCase: AddFavoriteUseCase,
    var selectAllFavoritesUseCase: SelectAllFavoritesUseCase,
    var removeFavoriteUseCase: RemoveFavoriteUseCase,
    var searchDrinkByIngredientsUseCase: SearchDrinkByIngredientsUseCase,
    var getDrinkDetailsUseCase: GetDrinkDetailsUseCase,
    var searchDrinkByNameUseCase: SearchDrinkByNameUseCase,
    var drinkModelMapper: DrinkModelMapper,
    @Named(DISPATCHERS_IO) var mainCoroutineContext: CoroutineContext
) : ViewModel(),
    CoroutineScope {

    private val job = Job()
    private var searchDrinkJob: Job = Job()

    private val _latestDrinks =
        MutableLiveData<ViewStateWrapper<List<DrinkModel>>>(ViewStateWrapper.Loading(0))
    val latestDrinks: LiveData<ViewStateWrapper<List<DrinkModel>>> get() = _latestDrinks

    private val _popularDrinks =
        MutableLiveData<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val popularDrinks: LiveData<ViewStateWrapper<List<DrinkModel>>> get() = _popularDrinks

    private val _randomDrinks =
        MutableLiveData<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val randomDrinks: LiveData<ViewStateWrapper<List<DrinkModel>>> get() = _randomDrinks

    private val _favoriteDrinks =
        MutableLiveData<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val favoriteDrinks: LiveData<ViewStateWrapper<List<DrinkModel>>> get() = _favoriteDrinks

    private val _drinkDetails =
        MutableLiveData<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Init
        )
    val drinkDetails: LiveData<ViewStateWrapper<List<DrinkModel>>> get() = _drinkDetails

    private val _drinksFilteredByIngredient =
        MutableLiveData<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val drinksFilteredByIngredient: LiveData<ViewStateWrapper<List<DrinkModel>>> get() = _drinksFilteredByIngredient

    private val _drinkSearchedByName =
        MutableLiveData<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Success(listOf())
        )
    val drinkSearchedByName: LiveData<ViewStateWrapper<List<DrinkModel>>> get() = _drinkSearchedByName

    private val _drinkToBeSearched =
        MutableLiveData<String>()
    val drinkToBeSearched: LiveData<String>
        get() = _drinkToBeSearched

    fun requestForLatestDrinks() = launch {
        latestDrinksUseCase.buildExecutable(GetLatestDrinksUseCase.Params())
            .onStart { _latestDrinks.postValue(ViewStateWrapper.Loading(0)) }
            .map { drink ->
                assignCollectionType(
                    drinkModelMapper.transform(drink),
                    DrinkCollectionType.LATEST
                )
            }
            .catch { throwable ->
                _latestDrinks.postValue(ViewStateWrapper.Error(throwable))
            }
            .collect { data ->
                _latestDrinks.postValue(ViewStateWrapper.Success(data))
            }
    }

    fun requestForPopularDrinks() = launch {
        popularDrinksUseCase.buildExecutable(GetPopularDrinksUseCase.Params())
            .onStart { _popularDrinks.postValue(ViewStateWrapper.Loading(0)) }
            .map { drink ->
                assignCollectionType(
                    drinkModelMapper.transform(drink),
                    DrinkCollectionType.POPULAR
                )
            }
            .catch { throwable ->
                _popularDrinks.postValue(ViewStateWrapper.Error(throwable))

            }
            .collect { data ->
                _popularDrinks.postValue(ViewStateWrapper.Success(data))
            }
    }

    fun requestForRandomDrinks() = launch {
        randomDrinksUseCase.buildExecutable(GetRandomDrinksUseCase.Params())
            .onStart { _randomDrinks.postValue(ViewStateWrapper.Loading(0)) }
            .map { drink ->
                assignCollectionType(
                    drinkModelMapper.transform(drink),
                    DrinkCollectionType.RANDOM
                )
            }
            .catch { throwable -> _randomDrinks.postValue(ViewStateWrapper.Error(throwable)) }
            .collect { data ->
                _randomDrinks.postValue(ViewStateWrapper.Success(data))
            }
    }

    fun filterDrinksByIngredient(p0: String) = launch {
        searchDrinkByIngredientsUseCase.buildExecutable(SearchDrinkByIngredientsUseCase.Params(p0))
            .onStart { _drinksFilteredByIngredient.postValue(ViewStateWrapper.Loading(0)) }
            .map { drink ->
                assignCollectionType(
                    drinkModelMapper.transform(drink),
                    DrinkCollectionType.FILTERED_BY_INGREDIENTS
                )
            }
            .catch { throwable ->
                _drinksFilteredByIngredient.postValue(ViewStateWrapper.Error(throwable))
            }
            .collect { data ->
                _drinksFilteredByIngredient.postValue(
                    ViewStateWrapper.Success(
                        data
                    )
                )
            }
    }

    fun requestForFavoriteDrinks() = launch {
        selectAllFavoritesUseCase.buildExecutable(SelectAllFavoritesUseCase.Params()).onStart {
            _favoriteDrinks.postValue(ViewStateWrapper.Loading(0))
        }.map {
            assignCollectionType(
                drinkModelMapper.transform(it),
                DrinkCollectionType.FAVORITE
            )
        }.catch { throwable ->
            _favoriteDrinks.postValue(ViewStateWrapper.Error(throwable))
        }.collect { data -> _favoriteDrinks.postValue(ViewStateWrapper.Success(data)) }
    }

    fun toggleFavoriteOfADrink(drink: DrinkModel): Flow<DrinkModel> =
        flow {
            if (drink.isFavourite) {
                drink.apply {
                    markFavorite(false)
                }

                removeFavoriteUseCase.buildExecutable(
                    RemoveFavoriteUseCase.Params(
                        drinkModelMapper.transform(
                            drink
                        )
                    )
                )
            } else {
                drink.apply {
                    markFavorite(true)
                }

                addFavoriteUseCase.buildExecutable(
                    AddFavoriteUseCase.Params(
                        drinkModelMapper.transform(
                            drink
                        )
                    )
                )
            }

            emit(drink)
        }

    fun requestForDrinkDetailsByName(p0: String?) = launch {
        p0?.let { id ->
            getDrinkDetailsUseCase.buildExecutable(GetDrinkDetailsUseCase.Params(id))
                .onStart { _drinkDetails.postValue(ViewStateWrapper.Loading(0)) }
                .map { drink ->
                    assignCollectionType(
                        drinkModelMapper.transform(drink),
                        DrinkCollectionType.NONE
                    )
                }
                .catch { throwable -> _drinkDetails.postValue(ViewStateWrapper.Error(throwable)) }
                .collect { data ->
                    _drinkDetails.postValue(ViewStateWrapper.Success(data))
                }
        }
    }

    fun searchForDrinkByName(p0: CharSequence) {
        if (searchDrinkJob.isActive) {
            searchDrinkJob.cancel(CancellationException())
        }

        searchDrinkJob = launch(mainCoroutineContext) {
            delay(SEARCH_DELAY_IN_MILLIS)

            searchDrinkByNameUseCase.buildExecutable(SearchDrinkByNameUseCase.Params(p0.toString()))
                .onStart { _drinkSearchedByName.postValue(ViewStateWrapper.Loading(0)) }
                .map { drink ->
                    assignCollectionType(
                        drinkModelMapper.transform(drink),
                        DrinkCollectionType.SEARCH_BY_NAME
                    )
                }
                .catch { throwable ->
                    _drinkSearchedByName.postValue(ViewStateWrapper.Error(throwable))
                }
                .collect { data ->
                    _drinkSearchedByName.postValue(ViewStateWrapper.Success(data))
                }
        }

    }

    fun setDrinkToBeSearched(p0: String) = launch {
        _drinkToBeSearched.postValue(p0)
    }

    fun syncDrinks(p0: DrinkModel) = launch {
        syncDrinks(p0, _latestDrinks)
        syncDrinks(p0, _popularDrinks)
        syncDrinks(p0, _drinksFilteredByIngredient)
        syncDrinks(p0, _drinkSearchedByName)
        syncDrinks(p0, _favoriteDrinks)
        syncDrinks(p0, _randomDrinks)
    }

    private fun syncDrinks(
        p0: DrinkModel,
        p1: MutableLiveData<ViewStateWrapper<List<DrinkModel>>>
    ) {
        if (p1.value is ViewStateWrapper.Success) {
            val v1: List<DrinkModel> =
                (p1.value as ViewStateWrapper.Success<List<DrinkModel>>).data
            var v2 = false
            v1.map {
                if (it.idDrink.equals(p0.idDrink)) {
                    v2 = true
                    it.markFavorite(p0.isFavourite)
                }
            }

            if (v2) {
                p1.postValue(ViewStateWrapper.Success(v1))
                return
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
