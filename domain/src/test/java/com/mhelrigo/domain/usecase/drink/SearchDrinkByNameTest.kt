package com.mhelrigo.domain.usecase.drink

import com.mhelrigo.domain.TestData
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.drink.SearchDrinkByNameUseCase
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

const val DRINK_NAME_SUCCESS = "Margarita"
const val DRINK_NAME_ERROR = "410 Gone"

@RunWith(MockitoJUnitRunner::class)
class SearchDrinkByNameTest {
    @Mock
    private lateinit var drinkRepository: DrinkRepository

    @Test
    fun searchByName_Success() = runBlocking {
        val data =
            Drinks(listOf(TestData.provideDrinks().find { a -> a.strDrink == DRINK_NAME_SUCCESS }!!))
        `when`(drinkRepository.searchByName(DRINK_NAME_SUCCESS)).thenReturn(ResultWrapper.build { data })

        val searchDrinkByNameUseCase = SearchDrinkByNameUseCase(drinkRepository)

        val result: ResultWrapper<Drinks, Exception> =
            searchDrinkByNameUseCase.buildExecutable(listOf(DRINK_NAME_SUCCESS))

        assertThat((result as ResultWrapper.Success).value, `is`(data))
    }

    @Test
    fun searchByName_Error() = runBlocking {
        val data =
            Drinks(listOf(TestData.provideDrinks().find { a -> a.strDrink == DRINK_NAME_ERROR }!!))
        `when`(drinkRepository.searchByName(DRINK_NAME_ERROR)).thenReturn(ResultWrapper.build {
            throw java.lang.Exception(
                "Might be anything exception"
            )
        })

        val searchDrinkByNameUseCase = SearchDrinkByNameUseCase(drinkRepository)

        val result: ResultWrapper<Drinks, Exception> =
            searchDrinkByNameUseCase.buildExecutable(listOf(DRINK_NAME_ERROR))

        assertThat((result as ResultWrapper.Error).error, CoreMatchers.isA(Exception::class.java))
    }
}