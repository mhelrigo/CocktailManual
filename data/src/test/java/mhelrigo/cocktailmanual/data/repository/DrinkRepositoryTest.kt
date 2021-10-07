package mhelrigo.cocktailmanual.data.repository

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.entity.drink.DrinksApiEntity
import mhelrigo.cocktailmanual.data.mapper.DrinkEntityMapper
import mhelrigo.cocktailmanual.data.repository.drink.DrinkRepositoryImpl
import mhelrigo.cocktailmanual.data.repository.drink.local.DrinkDao
import mhelrigo.cocktailmanual.data.repository.drink.remote.DrinkApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verifyNoMoreInteractions
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner;

const val SEARCH_BY_NAME_PARAM = "Margarita"
const val SEARCH_BY_INGREDIENT = "Gin"
const val DRINK_ID = "11007"

@RunWith(MockitoJUnitRunner::class)
class DrinkRepositoryTest {
    @Mock
    private lateinit var drinkApi: DrinkApi

    @Mock
    private lateinit var drinkDao: DrinkDao

    @Mock
    lateinit var drinkEntityMapper: DrinkEntityMapper

    @Mock
    private lateinit var drinksApiEntity: DrinksApiEntity

    private lateinit var drinkRepositoryImpl: DrinkRepositoryImpl

    @Before
    fun setUp() {
        drinkRepositoryImpl = DrinkRepositoryImpl(drinkApi, drinkDao, drinkEntityMapper)
    }

    @Test
    fun getRandomly_Success() = runBlocking {
        given(drinkApi.getRandomly()).willReturn(drinksApiEntity)
        drinkRepositoryImpl.getRandomly().collect {
            verify(drinkApi).getRandomly()
            verifyNoMoreInteractions(drinkApi)
        }
    }

    @Test
    fun getByPopularity_Success() = runBlocking {
        given(drinkApi.getByPopularity()).willReturn(drinksApiEntity)
        drinkRepositoryImpl.getByPopularity().collect {
            verify(drinkApi).getByPopularity()
            verifyNoMoreInteractions(drinkApi)
        }
    }

    @Test
    fun getLatest_Success() = runBlocking {
        given(drinkApi.getLatest()).willReturn(drinksApiEntity)
        drinkRepositoryImpl.getLatest().collect {
            verify(drinkApi).getLatest()
            verifyNoMoreInteractions(drinkApi)
        }
    }

    @Test
    fun searchByIngredient_Success() = runBlocking {
        given(drinkApi.searchByIngredient(SEARCH_BY_NAME_PARAM)).willReturn(drinksApiEntity)
        drinkRepositoryImpl.searchByIngredient(SEARCH_BY_NAME_PARAM).collect {
            verify(drinkApi).searchByIngredient(SEARCH_BY_NAME_PARAM)
            verifyNoMoreInteractions(drinkApi)
        }
    }

    @Test
    fun searchByName_Success() = runBlocking {
        given(drinkApi.searchByName(SEARCH_BY_INGREDIENT)).willReturn(drinksApiEntity)
        drinkRepositoryImpl.searchByName(SEARCH_BY_INGREDIENT).collect {
            verify(drinkApi).searchByName(SEARCH_BY_INGREDIENT)
            verifyNoMoreInteractions(drinkApi)
        }
    }

    @Test
    fun getDetails_Success() = runBlocking {
        given(drinkApi.getDetails(DRINK_ID)).willReturn(drinksApiEntity)
        drinkRepositoryImpl.getDetails(DRINK_ID).collect {
            verify(drinkApi).getDetails(DRINK_ID)
            verifyNoMoreInteractions(drinkApi)
        }
    }
}