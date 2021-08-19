package mhelrigo.cocktailmanual.data.repository

import com.google.gson.Gson
import com.mhelrigo.commons.MockFileReader
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.ingredient.IngredientRepositoryImpl
import mhelrigo.cocktailmanual.data.repository.ingredient.remote.IngredientApi
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.model.Ingredients
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

const val GET_DETAILS_PARAM = "Vodka"

class IngredientRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var ingredientsRepositoryImpl: IngredientRepositoryImpl

    @Before
    fun init() {
        mockRetrofit()
        ingredientsRepositoryImpl = IngredientRepositoryImpl(
            retrofit.create(IngredientApi::class.java),
            Dispatchers.Unconfined
        )
    }

    @Test
    fun getAll_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("GetAllIngredientMockResult.json"))))

        when (val result: ResultWrapper<Exception, Ingredients> =
            ingredientsRepositoryImpl.getAll()) {
            is ResultWrapper.Success -> {
                assertEquals(result.value.drinks.isNotEmpty(), true)
            }
            is ResultWrapper.Error -> {
                MatcherAssert.assertThat(result.error, `isA`(Exception::class.java))
            }
        }
    }

    @Test
    fun getDetails_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("GetDetailsIngredientMockResult.json").content)))
        
        when (val result: ResultWrapper<Exception, Ingredients> =
            ingredientsRepositoryImpl.getDetails(GET_DETAILS_PARAM)) {
            is ResultWrapper.Success -> {
                assertEquals(
                    result.value.ingredients[0].strIngredient,
                    GET_DETAILS_PARAM
                )
            }
            is ResultWrapper.Error -> {
                MatcherAssert.assertThat(result.error, `isA`(Exception::class.java))
            }
        }
    }

    private fun mockRetrofit() {
        retrofit = MockRetrofit.let {
            it.mockWebServer = mockWebServer
            it.mockRetrofit()
        }
    }
}