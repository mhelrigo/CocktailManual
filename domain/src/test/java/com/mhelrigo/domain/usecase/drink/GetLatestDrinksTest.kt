package com.mhelrigo.domain.usecase.drink

import com.mhelrigo.domain.TestData
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.drink.GetLatestDrinksUseCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetLatestDrinksTest {
    @Mock
    private lateinit var drinkRepository: DrinkRepository

    @Test
    fun getLatestDrinksUseCase_Success() = runBlocking {
        val list = Drinks(TestData.provideDrinks())

        `when`(drinkRepository.getLatest()).thenReturn(ResultWrapper.build { list })

        val getLatestDrinksUseCase = GetLatestDrinksUseCase(drinkRepository)

        val result: ResultWrapper<Drinks, Exception> = getLatestDrinksUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Success).value, `is`(Drinks(TestData.provideDrinks())))
    }

    @Test
    fun getLatestDrinksUseCase_Error() = runBlocking {
        `when`(drinkRepository.getLatest()).thenReturn(ResultWrapper.build {
            throw java.lang.Exception(
                "Might be anything exception"
            )
        })

        val getLatestDrinksUseCase = GetLatestDrinksUseCase(drinkRepository)

        val result: ResultWrapper<Drinks, Exception> = getLatestDrinksUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Error).error, `isA`(Exception::class.java))
    }
}