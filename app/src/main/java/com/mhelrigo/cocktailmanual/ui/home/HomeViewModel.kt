package com.mhelrigo.cocktailmanual.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhelrigo.cocktailmanual.ui.model.FromCollectionOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import mhelrigo.cocktailmanual.domain.model.Drink
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.drink.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    var getPopularDrinksUseCase: GetPopularDrinksUseCase,
    var latestDrinksUseCase: GetLatestDrinksUseCase,
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

    private val _expandedDrinkDetails =
        MutableLiveData<com.mhelrigo.cocktailmanual.ui.model.Drink>()
    val expandedDrinkDetails: LiveData<com.mhelrigo.cocktailmanual.ui.model.Drink> get() = _expandedDrinkDetails

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchLatestDrinks() = launch {
        when (val result = latestDrinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _latestDrinks.postValue(
                    markAllFavorites(
                        result.value.drinks,
                        fetchFavorites()
                    ).map {
                        com.mhelrigo.cocktailmanual.ui.model.Drink.fromDrinkDomainModel(it).apply {
                            assignDrawableColor()
                            fromCollectionOf = FromCollectionOf.LATEST
                        }
                    }
                )
            }
            is ResultWrapper.Error -> {
                Timber.e("Error ${result.error}")
            }
        }
    }

    fun fetchPopularDrinks() = launch {
        when (val result = getPopularDrinksUseCase.buildExecutable(null)) {
            is ResultWrapper.Success -> {
                _popularDrinks.postValue(
                    markAllFavorites(
                        result.value.drinks,
                        fetchFavorites()
                    ).map {
                        com.mhelrigo.cocktailmanual.ui.model.Drink.fromDrinkDomainModel(it).apply {
                            assignDrawableColor()
                            fromCollectionOf = FromCollectionOf.POPULAR
                        }
                    }
                )
            }
            is ResultWrapper.Error -> {
                Timber.e("Error ${result.error}")
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

    private suspend fun fetchFavorites(): List<Drink> {
        val result = selectAllFavoritesUseCase.buildExecutable()

        return if (result is ResultWrapper.Success) {
            result.value
        } else {
            emptyList()
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
            if (drink.isFavourite)
                removeFavoriteUseCase.buildExecutable(listOf(drink.idDrink!!))
            else
                addFavoriteUseCase.buildExecutable(listOf(drink.idDrink!!))
        }

        runBlocking {
            result = deferredResult.await()
        }

        return when (result) {
            is ResultWrapper.Success -> ResultWrapper.build {
                drink.apply {
                    markFavorite(!isFavourite)
                }
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