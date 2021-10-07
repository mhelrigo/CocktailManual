package mhelrigo.cocktailmanual.data.repository

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.entity.ingredient.IngredientsApiEntity
import mhelrigo.cocktailmanual.data.mapper.IngredientMapper
import mhelrigo.cocktailmanual.data.repository.ingredient.IngredientRepositoryImpl
import mhelrigo.cocktailmanual.data.repository.ingredient.remote.IngredientApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verifyNoMoreInteractions
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

const val GET_DETAILS_PARAM = "Vodka"

@RunWith(MockitoJUnitRunner::class)
class IngredientRepositoryTest {
    private lateinit var ingredientsRepositoryImpl: IngredientRepositoryImpl

    @Mock
    private lateinit var ingredientApi: IngredientApi

    @Mock
    private lateinit var ingredientMapper: IngredientMapper

    @Mock
    private lateinit var ingredientsApiEntity: IngredientsApiEntity

    @Before
    fun init() {
        ingredientMapper = IngredientMapper()
        ingredientsRepositoryImpl = IngredientRepositoryImpl(
            ingredientApi,
            ingredientMapper
        )
    }

    @Test
    fun getAll_Success() = runBlocking {
        given(ingredientApi.getAll()).willReturn(ingredientsApiEntity)
        ingredientsRepositoryImpl.getAll().collect {
            verify(ingredientApi).getAll()
            verifyNoMoreInteractions(ingredientApi)
        }
    }

    @Test
    fun getDetails_Success() = runBlocking {
        given(ingredientApi.getDetails(GET_DETAILS_PARAM)).willReturn(ingredientsApiEntity)
        ingredientsRepositoryImpl.getDetails(GET_DETAILS_PARAM).collect {
            verify(ingredientApi).getDetails(GET_DETAILS_PARAM)
            verifyNoMoreInteractions(ingredientApi)
        }
    }
}