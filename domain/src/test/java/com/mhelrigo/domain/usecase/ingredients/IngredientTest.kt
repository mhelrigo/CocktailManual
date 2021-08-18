package com.mhelrigo.domain.usecase.ingredients

import com.mhelrigo.domain.TestData
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.domain.model.Ingredients
import mhelrigo.cocktailmanual.domain.repository.IngredientRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.ingredients.GetAllIngredientUseCase
import mhelrigo.cocktailmanual.domain.usecase.ingredients.GetIngredientDetailUseCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

const val INGREDIENT_NAME = "Gin"

@RunWith(MockitoJUnitRunner::class)
class IngredientTest {
    @Mock
    private lateinit var ingredientRepository: IngredientRepository

    @Test
    fun getAllIngredient_Success() = runBlocking {
        val data = TestData.provideIngredients()

        `when`(ingredientRepository.getAll()).thenReturn(ResultWrapper.build { data })

        val getAllIngredientUseCase = GetAllIngredientUseCase(ingredientRepository)

        val result = getAllIngredientUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Success).value, `is`(data))
    }

    @Test
    fun getAllIngredient_Error() = runBlocking {
        `when`(ingredientRepository.getAll()).thenReturn(ResultWrapper.build { throw Exception("Any kind of error") })

        val getAllIngredientUseCase = GetAllIngredientUseCase(ingredientRepository)

        val result = getAllIngredientUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Error).error, `isA`(Exception::class.java))
    }

    @Test
    fun getIngredientDetails() = runBlocking {
        val data =
            Ingredients(listOf(TestData.provideIngredients().ingredients.find { a -> a.strIngredient == INGREDIENT_NAME }!!))

        `when`(ingredientRepository.getDetails(INGREDIENT_NAME)).thenReturn(ResultWrapper.build { data })

        val getIngredientDetailUseCase = GetIngredientDetailUseCase(ingredientRepository)

        val result = getIngredientDetailUseCase.buildExecutable(listOf(INGREDIENT_NAME))

        assertThat((result as ResultWrapper.Success).value, `is`(data))
    }
}