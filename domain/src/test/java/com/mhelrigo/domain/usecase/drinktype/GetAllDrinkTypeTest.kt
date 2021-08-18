package com.mhelrigo.domain.usecase.drinktype

import com.mhelrigo.domain.TestData
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.domain.repository.DrinkTypeRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.drinktype.GetAllDrinkTypeUseCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllDrinkTypeTest {
    @Mock
    private lateinit var drinkTypeRepository: DrinkTypeRepository

    @Test
    fun getAllDrinkType_Success() = runBlocking {
        val data = TestData.provideDrinkTypes()

        `when`(drinkTypeRepository.getAll()).thenReturn(ResultWrapper.build { data })

        val getAllDrinkTypeUseCase = GetAllDrinkTypeUseCase(drinkTypeRepository)

        val result = getAllDrinkTypeUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Success).value, `is`(data))
    }

    @Test
    fun getAllDrinkType_Error() = runBlocking {
        `when`(drinkTypeRepository.getAll()).thenReturn(ResultWrapper.build { throw Exception("Any kind of Exception") })

        val getAllDrinkTypeUseCase = GetAllDrinkTypeUseCase(drinkTypeRepository)

        val result = getAllDrinkTypeUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Error).error, `isA`(java.lang.Exception::class.java))
    }
}