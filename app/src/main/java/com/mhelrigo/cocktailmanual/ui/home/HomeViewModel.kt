package com.mhelrigo.cocktailmanual.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhelrigo.cocktailmanual.ui.model.FromCollectionOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import mhelrigo.cocktailmanual.domain.model.Drink
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.drink.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    var popularDrinksUseCase: GetPopularDrinksUseCase,
    var latestDrinksUseCase: GetLatestDrinksUseCase,
    var randomDrinksUseCase: GetRandomDrinksUseCase,
    var addFavoriteUseCase: AddFavoriteUseCase,
    var selectAllFavoritesUseCase: SelectAllFavoritesUseCase,
    var removeFavoriteUseCase: RemoveFavoriteUseCase,
    @Named("Dispatchers.IO") var mainCoroutineContext: CoroutineContext
) : ViewModel(),
    CoroutineScope {

    private val job = Job()

    private val _latestDrinks = MutableLiveData<List<com.mhelrigo.cocktailmanual.ui.model.Drink>>()
    val latestDrinks: LiveData<List<com.mhelrigo.cocktailmanual.ui.model.Drink>> get() = _latestDrinks

    private val _popularDrinks = MutableLiveData<List<com.mhelrigo.cocktailmanual.ui.model.Drink>>()
    val popularDrinks: LiveData<List<com.mhelrigo.cocktailmanual.ui.model.Drink>> get() = _popularDrinks

    private val _randomDrinks =
        MutableLiveData<List<com.mhelrigo.cocktailmanual.ui.model.Drink>>()
    val randomDrinks: LiveData<List<com.mhelrigo.cocktailmanual.ui.model.Drink>> get() = _randomDrinks

    private val _favoriteDrinks =
        MutableLiveData<List<com.mhelrigo.cocktailmanual.ui.model.Drink>>()
    val favoriteDrinks: LiveData<List<com.mhelrigo.cocktailmanual.ui.model.Drink>> get() = _favoriteDrinks

    private val _expandedDrinkDetails =
        MutableLiveData<com.mhelrigo.cocktailmanual.ui.model.Drink>()
    val expandedDrinkDetails: LiveData<com.mhelrigo.cocktailmanual.ui.model.Drink> get() = _expandedDrinkDetails

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun requestForLatestDrinks() = launch {
        when (val result = latestDrinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _latestDrinks.postValue(
                    beautifyDrinkResult(result.value, FromCollectionOf.LATEST)
                )
            }
            is ResultWrapper.Error -> {
                Timber.e("Error ${result.error}")
            }
        }
    }

    fun requestForPopularDrinks() = launch {
        when (val result = popularDrinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _popularDrinks.postValue(
                    beautifyDrinkResult(result.value, FromCollectionOf.POPULAR)
                )
            }
            is ResultWrapper.Error -> {
                Timber.e("Error ${result.error}")
            }
        }
    }

    /**
     * This request will cache previous result for pagination suitable for infinite scrolling.
     * */
    fun requestForRandomDrinks() = launch {
        when (val result = randomDrinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _randomDrinks.postValue(beautifyDrinkResult(result.value, FromCollectionOf.RANDOM))
            }
            is ResultWrapper.Error -> {
                Timber.e("Error ${result.error}")
            }
        }
    }

    /**
     * What this does is it will make Drinks from remote into a presentable model that will be used
     * by the Views. It will do the following:
     * 1. Call for the method [markAllFavorites] to match the local favorites with result from remote by [idDrink].
     * 2. Assign a background color by using [assignDrawableColor].
     * 3. Each Drink will be assigned an enum of [FromCollectionOf], the value depends on where the result came from(LATEST or POPULAR or RANDOM).
     *
     * [FromCollectionOf] is needed for when updating specific Drink item from there respective Collections
     *
     * @param [result] the return value from the remote request
     * @param [fromCollectionOfValue] the type of request and the source of the return value
     * @return [List<com.mhelrigo.cocktailmanual.ui.model.Drink>] the processed list that will be used by a View
     * */
    private fun beautifyDrinkResult(
        result: Drinks,
        fromCollectionOfValue: FromCollectionOf
    ): List<com.mhelrigo.cocktailmanual.ui.model.Drink> {
        return markAllFavorites(result.drinks, requestForFavoriteDrinks()).map {
            com.mhelrigo.cocktailmanual.ui.model.Drink.fromDrinkDomainModel(it).apply {
                assignDrawableColor()
                fromCollectionOf = fromCollectionOfValue
            }
        }
    }

    /**
     * Basically, this method returns a List of Drinks while at the same time all favorites are marked.
     * It does so by:
     * 1. Maps the [favorites]
     * 2. Then filters the [toBeMerged] to see the similarities with [favorites]
     * 3. Once filtered, the result will be mapped then the items inside will process [markFavorite] with a value of [true]
     *
     * @param [toBeMerged] The latest drinks from remote source
     * @param [favorites] Comes from local source
     * @return [List<Drink>] Combine value of [toBeMerged] and [favorites] without duplication
     * */
    private fun markAllFavorites(toBeMerged: List<Drink>, favorites: List<Drink>): List<Drink> {
        favorites.map { favorites ->
            toBeMerged.filter { latest ->
                latest.idDrink == favorites.idDrink
            }.map {
                it.markFavorite(true)
            }
        }

        return toBeMerged
    }

    fun requestForFavoriteDrinks(): List<Drink> {
        var deferredResult = listOf<Drink>()
        val asyncResult = async {
            val result = selectAllFavoritesUseCase.buildExecutable()
            deferredResult = if (result is ResultWrapper.Success) {
                _favoriteDrinks.postValue(result.value.map {
                    com.mhelrigo.cocktailmanual.ui.model.Drink.fromDrinkDomainModel(it)
                })

                result.value
            } else {
                _favoriteDrinks.postValue(emptyList())

                emptyList()
            }
        }

        launch {
            asyncResult.await()
        }

        return deferredResult
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

    fun expandDrinkDetails(drink: com.mhelrigo.cocktailmanual.ui.model.Drink) {
        _expandedDrinkDetails.postValue(drink)
    }

    override val coroutineContext: CoroutineContext
        get() = mainCoroutineContext + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}