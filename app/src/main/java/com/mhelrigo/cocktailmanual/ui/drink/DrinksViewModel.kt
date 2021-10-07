package com.mhelrigo.cocktailmanual.ui.drink

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
        MutableStateFlow<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val latestDrinks: StateFlow<ViewStateWrapper<List<DrinkModel>>> get() = _latestDrinks

    private val _popularDrinks =
        MutableStateFlow<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val popularDrinks: StateFlow<ViewStateWrapper<List<DrinkModel>>> get() = _popularDrinks

    private val _randomDrinks =
        MutableStateFlow<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val randomDrinks: StateFlow<ViewStateWrapper<List<DrinkModel>>> get() = _randomDrinks

    private val _favoriteDrinks =
        MutableStateFlow<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val favoriteDrinks: StateFlow<ViewStateWrapper<List<DrinkModel>>> get() = _favoriteDrinks

    private val _drinkDetails =
        MutableStateFlow<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Init
        )
    val drinkDetails: StateFlow<ViewStateWrapper<List<DrinkModel>>> get() = _drinkDetails

    private val _drinksFilteredByIngredient =
        MutableStateFlow<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Loading(0)
        )
    val drinksFilteredByIngredient: StateFlow<ViewStateWrapper<List<DrinkModel>>> get() = _drinksFilteredByIngredient

    private val _drinkSearchedByName =
        MutableStateFlow<ViewStateWrapper<List<DrinkModel>>>(
            ViewStateWrapper.Success(listOf())
        )
    val drinkSearchedByName: StateFlow<ViewStateWrapper<List<DrinkModel>>> get() = _drinkSearchedByName

    private val _drinkToBeSearched =
        MutableStateFlow<String>("")
    val drinkToBeSearched: StateFlow<String>
        get() = _drinkToBeSearched

    private val _toggledDrink = MutableSharedFlow<DrinkModel>()
    val toggledDrink: SharedFlow<DrinkModel> get() = _toggledDrink

    fun requestForLatestDrinks() = launch {
        latestDrinksUseCase.buildExecutable(GetLatestDrinksUseCase.Params())
            .onStart { _latestDrinks.value = ViewStateWrapper.Loading(0) }
            .map { drink ->
                assignCollectionType(
                    drinkModelMapper.transform(drink),
                    DrinkCollectionType.LATEST
                )
            }
            .catch { throwable ->
                _latestDrinks.value = ViewStateWrapper.Error(throwable)
            }.collect { data ->
                _latestDrinks.value = ViewStateWrapper.Success(data)
            }
    }

    fun requestForPopularDrinks() = launch {
        popularDrinksUseCase.buildExecutable(GetPopularDrinksUseCase.Params())
            .onStart { _popularDrinks.value = ViewStateWrapper.Loading(0) }
            .map { drink ->
                assignCollectionType(
                    drinkModelMapper.transform(drink),
                    DrinkCollectionType.POPULAR
                )
            }
            .catch { throwable ->
                _popularDrinks.value = ViewStateWrapper.Error(throwable)
                Timber.e("requestForPopularDrinks ${throwable.message}")

            }
            .collect { data ->
                _popularDrinks.value = ViewStateWrapper.Success(data)
            }
    }

    fun requestForRandomDrinks() = launch {
        randomDrinksUseCase.buildExecutable(GetRandomDrinksUseCase.Params())
            .onStart { _randomDrinks.value = ViewStateWrapper.Loading(0) }
            .map { drink ->
                assignCollectionType(
                    drinkModelMapper.transform(drink),
                    DrinkCollectionType.RANDOM
                )
            }
            .catch { throwable -> _randomDrinks.value = ViewStateWrapper.Error(throwable) }
            .collect { data ->
                _randomDrinks.value = ViewStateWrapper.Success(data)
            }
    }

    fun filterDrinksByIngredient(p0: String) = launch {
        searchDrinkByIngredientsUseCase.buildExecutable(SearchDrinkByIngredientsUseCase.Params(p0))
            .onStart { _drinksFilteredByIngredient.value = ViewStateWrapper.Loading(0) }
            .map { drink ->
                assignCollectionType(
                    drinkModelMapper.transform(drink),
                    DrinkCollectionType.FILTERED_BY_INGREDIENTS
                )
            }
            .catch { throwable ->
                _drinksFilteredByIngredient.value = ViewStateWrapper.Error(throwable)
            }
            .collect { data ->
                _drinksFilteredByIngredient.value = ViewStateWrapper.Success(
                    data
                )
            }
    }

    fun requestForFavoriteDrinks() = launch {
        selectAllFavoritesUseCase.buildExecutable(SelectAllFavoritesUseCase.Params()).onStart {
            _favoriteDrinks.value = ViewStateWrapper.Loading(0)
        }.map {
            assignCollectionType(
                drinkModelMapper.transform(it),
                DrinkCollectionType.FAVORITE
            )
        }.catch { throwable ->
            _favoriteDrinks.value = ViewStateWrapper.Error(throwable)
        }.collect { data -> _favoriteDrinks.value = ViewStateWrapper.Success(data) }
    }

    fun toggleFavoriteOfADrink(drink: DrinkModel): Flow<DrinkModel> =
        flow {
            if (drink.isFavourite) {
                drink.apply {
                    markFavorite(false)
                }

                removeFavoriteUseCase.buildExecutable(RemoveFavoriteUseCase.Params(drinkModelMapper.transform(drink)))
            } else {
                drink.apply {
                    markFavorite(true)
                }

                addFavoriteUseCase.buildExecutable(AddFavoriteUseCase.Params(drinkModelMapper.transform(drink)))
            }

            emit(drink)
        }

    fun requestForDrinkDetailsByName(p0: String?) = launch {
        p0?.let { id ->
            getDrinkDetailsUseCase.buildExecutable(GetDrinkDetailsUseCase.Params(id))
                .onStart { _drinkDetails.value = ViewStateWrapper.Loading(0) }
                .map { drink ->
                    assignCollectionType(
                        drinkModelMapper.transform(drink),
                        DrinkCollectionType.NONE
                    )
                }
                .catch { throwable -> _drinkDetails.value = ViewStateWrapper.Error(throwable) }
                .collect { data ->
                    _drinkDetails.value =
                        ViewStateWrapper.Success(data)
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
                .onStart { _drinkSearchedByName.value = ViewStateWrapper.Loading(0) }
                .map { drink ->
                    assignCollectionType(
                        drinkModelMapper.transform(drink),
                        DrinkCollectionType.SEARCH_BY_NAME
                    )
                }
                .catch { throwable ->
                    _drinkSearchedByName.value = ViewStateWrapper.Error(throwable)
                }
                .collect { data ->
                    _drinkSearchedByName.value = ViewStateWrapper.Success(data)
                }
        }
    }

    fun setDrinkToBeSearched(p0: String) = launch {
        _drinkToBeSearched.value = p0
    }

    fun setToggledDrink(p0: DrinkModel) = launch {
        _toggledDrink.emit(p0)
    }

    override val coroutineContext: CoroutineContext
        get() = mainCoroutineContext + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
