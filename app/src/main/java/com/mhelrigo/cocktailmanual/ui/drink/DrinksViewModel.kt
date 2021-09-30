package com.mhelrigo.cocktailmanual.ui.drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mhelrigo.cocktailmanual.ui.model.Drink.Factory.assignCollectionType
import com.mhelrigo.cocktailmanual.ui.model.Drink.Factory.markAllFavorites
import com.mhelrigo.cocktailmanual.ui.model.Drink.Factory.transformCollectionIntoPresentationObject
import com.mhelrigo.cocktailmanual.ui.model.DrinkCollectionType
import com.mhelrigo.commons.SEARCH_DELAY_IN_MILLIS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import mhelrigo.cocktailmanual.domain.model.Drink
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.drink.*
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
    @Named("Dispatchers.IO") var mainCoroutineContext: CoroutineContext
) : ViewModel(),
    CoroutineScope {

    private val job = Job()
    private var searchDrinkJob: Job = Job()

    private val _latestDrinks =
        MutableLiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>>()
    val latestDrinks: LiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>> get() = _latestDrinks

    private val _popularDrinks =
        MutableLiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>>()
    val popularDrinks: LiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>> get() = _popularDrinks

    private val _randomDrinks =
        MutableLiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>>()
    val randomDrinks: LiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>> get() = _randomDrinks

    private val _favoriteDrinks =
        MutableLiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>>()
    val favoriteDrinks: LiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>> get() = _favoriteDrinks

    private val _drinkDetails =
        MutableLiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>>()
    val drinkDetails: LiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>> get() = _drinkDetails

    private val _drinksFilteredByIngredient =
        MutableLiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>>()
    val drinksFilteredByIngredient: LiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>> get() = _drinksFilteredByIngredient

    private val _drinkSearchedByName =
        MutableLiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>>()
    val drinkSearchedByName: LiveData<ResultWrapper<Exception, List<com.mhelrigo.cocktailmanual.ui.model.Drink>>> get() = _drinkSearchedByName

    private val _isConnectedToInternet = MutableLiveData(false)
    val isConnectedToInternet: LiveData<Boolean> get() = _isConnectedToInternet

    private val _drinkToBeSearched = MutableLiveData<String>()
    val drinkToBeSearched: LiveData<String> get() = _drinkToBeSearched

    private val _toggledDrink = MutableSharedFlow<com.mhelrigo.cocktailmanual.ui.model.Drink>()
    val toggledDrink: SharedFlow<com.mhelrigo.cocktailmanual.ui.model.Drink> get() = _toggledDrink

    fun requestForLatestDrinks() = launch {
        _latestDrinks.postValue(ResultWrapper.buildLoading())
        when (val result = latestDrinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _latestDrinks.postValue(ResultWrapper.build {
                    handleResult(
                        result.value,
                        DrinkCollectionType.LATEST
                    )
                })
            }
            is ResultWrapper.Error -> {
                FirebaseCrashlytics.getInstance().recordException(result.error)
                _latestDrinks.postValue(ResultWrapper.build { throw Exception(result.error) })
            }
        }
    }

    fun requestForPopularDrinks() = launch {
        _popularDrinks.postValue(ResultWrapper.buildLoading())
        when (val result = popularDrinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _popularDrinks.postValue(ResultWrapper.build {
                    handleResult(
                        result.value,
                        DrinkCollectionType.POPULAR
                    )
                })
            }
            is ResultWrapper.Error -> {
                FirebaseCrashlytics.getInstance().recordException(result.error)
                _popularDrinks.postValue(ResultWrapper.build { throw Exception(result.error) })
            }
        }
    }

    fun requestForRandomDrinks() = launch {
        _randomDrinks.postValue(ResultWrapper.buildLoading())
        when (val result = randomDrinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _randomDrinks.postValue(ResultWrapper.build {
                    handleResult(
                        result.value,
                        DrinkCollectionType.RANDOM
                    )
                })
            }
            is ResultWrapper.Error -> {
                FirebaseCrashlytics.getInstance().recordException(result.error)
                _randomDrinks.postValue(ResultWrapper.build { throw Exception(result.error) })
            }
        }
    }

    fun filterDrinksByIngredient(p0: String) = launch {
        _drinksFilteredByIngredient.postValue(ResultWrapper.buildLoading())
        when (val result = searchDrinkByIngredientsUseCase.buildExecutable(listOf(p0))) {
            is ResultWrapper.Success -> {
                _drinksFilteredByIngredient.postValue(ResultWrapper.build {
                    handleResult(
                        result.value,
                        DrinkCollectionType.FILTERED_BY_INGREDIENTS
                    )
                })
            }
            is ResultWrapper.Error -> {
                FirebaseCrashlytics.getInstance().recordException(result.error)
                _drinksFilteredByIngredient.postValue(ResultWrapper.build { throw Exception(result.error) })
            }
        }
    }

    /**
     * This will make necessary adjustments on the result by doing the following:
     * 1. [markAllFavorites] will mark all favorites in the result
     * 2. [assignCollectionType] will assign what kind of collection the result is
     * 3. [transformCollectionIntoPresentationObject] will transform the result into a presentation object
     *
     * This adjustments are needed for transforming the domain object into a presentable data.
     * [markAllFavorites] calls for [requestForFavoriteDrinks] as parameter to get the list of favorites.
     *
     * @param [result] the result
     * @param [drinkCollectionTypeValue] tye of collection
     * */
    private fun handleResult(
        result: Drinks,
        drinkCollectionTypeValue: DrinkCollectionType
    ): List<com.mhelrigo.cocktailmanual.ui.model.Drink> {
        return markAllFavorites(result.drinks, requestForFavoriteDrinks()).run {
            assignCollectionType(
                transformCollectionIntoPresentationObject(this),
                drinkCollectionTypeValue
            )
        }
    }

    /**
     * Will request for the list of favorites from local source, and will also update [_favoriteDrinks].
     * So every time this is called, [_favoriteDrinks] is updated.
     * */
    fun requestForFavoriteDrinks(): List<com.mhelrigo.cocktailmanual.ui.model.Drink> {
        _favoriteDrinks.postValue(ResultWrapper.buildLoading())

        var result: ResultWrapper<Exception, List<Drink>>?
        val deferredResult = async {
            selectAllFavoritesUseCase.buildExecutable()
        }

        runBlocking {
            result = deferredResult.await()
        }

        return when (result) {
            is ResultWrapper.Success -> {
                return (result as ResultWrapper.Success<List<Drink>>).value.run {
                    assignCollectionType(
                        transformCollectionIntoPresentationObject(this),
                        DrinkCollectionType.FAVORITE
                    )
                }.also {
                    _favoriteDrinks.postValue(ResultWrapper.build {
                        it
                    })
                }
            }

            is ResultWrapper.Error -> {
                emptyList()
            }
            else -> {
                emptyList()
            }
        }
    }

    /**
     * Removes or Adds a Drink from Favorites
     * 1. [deferredResult] it runs asyncly to wait for the operation's result so it can be processed by the View
     * 2. although inside of [runBlocking] the actual operations runs in Dispatchers.IO see @[DrinkRepositoryImpl]
     * 3. If operation a success, it assign [isFavourite] a negated value of itself
     * 4. then results are returned to be processed by the view
     *
     * @param [drink] The Drink to be processed
     * @return [ResultWrapper<Exception, Drink>?] View will handle operation's result
     * */
    fun toggleFavoriteOfADrink(drink: com.mhelrigo.cocktailmanual.ui.model.Drink): ResultWrapper<Exception, com.mhelrigo.cocktailmanual.ui.model.Drink>? {
        var result: ResultWrapper<Exception, Unit>?

        val deferredResult = async {
            if (drink.isFavourite) {
                drink.apply {
                    markFavorite(false)
                }

                removeFavoriteUseCase.buildExecutable(listOf(drink.toDrinkDomainModel()))
            } else {
                drink.apply {
                    markFavorite(true)
                }

                addFavoriteUseCase.buildExecutable(listOf(drink.toDrinkDomainModel()))
            }
        }

        runBlocking {
            result = deferredResult.await()
        }

        return when (result) {
            is ResultWrapper.Success -> ResultWrapper.build {
                drink
            }
            is ResultWrapper.Error -> {
                ResultWrapper.build { throw Exception((result as ResultWrapper.Error<Exception>).error) }
            }
            else -> {
                ResultWrapper.build {
                    drink
                }
            }
        }
    }

    fun requestForDrinkDetailsByName(p0: String?) = launch {
        _drinkDetails.postValue(ResultWrapper.buildLoading())

        p0?.let { id ->
            when (val result = getDrinkDetailsUseCase.buildExecutable(
                listOf(id)
            )) {
                is ResultWrapper.Success -> {
                    _drinkDetails.postValue(ResultWrapper.build {
                        handleResult(result.value, DrinkCollectionType.NONE)
                    })
                }
                is ResultWrapper.Error -> {
                    _drinkDetails.postValue(ResultWrapper.build { throw Exception(result.error) })
                }
            }
        }
    }

    fun searchForDrinkByName(p0: CharSequence) {
        if (searchDrinkJob.isActive) {
            searchDrinkJob.cancel(CancellationException())
        }

        searchDrinkJob = launch(mainCoroutineContext) {
            delay(SEARCH_DELAY_IN_MILLIS)

            _drinkSearchedByName.postValue(ResultWrapper.buildLoading())

            when (val result = searchDrinkByNameUseCase.buildExecutable(listOf(p0.toString()))) {
                is ResultWrapper.Success -> {
                    _drinkSearchedByName.postValue(ResultWrapper.build {
                        handleResult(
                            result.value,
                            DrinkCollectionType.SEARCH_BY_NAME
                        )
                    })
                }
                is ResultWrapper.Error -> {
                    FirebaseCrashlytics.getInstance().recordException(result.error)
                    _drinkSearchedByName.postValue(ResultWrapper.build { throw Exception(result.error) })
                }
            }
        }
    }

    fun handleInternetConnectionChanges(p0: Boolean) {
        _isConnectedToInternet.postValue(p0)
    }

    fun setMealToBeSearched(p0: String) {
        _drinkToBeSearched.postValue(p0)
    }

    fun setToggledDrink(p0: com.mhelrigo.cocktailmanual.ui.model.Drink) = launch {
        _toggledDrink.emit(p0)
    }

    override val coroutineContext: CoroutineContext
        get() = mainCoroutineContext + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
