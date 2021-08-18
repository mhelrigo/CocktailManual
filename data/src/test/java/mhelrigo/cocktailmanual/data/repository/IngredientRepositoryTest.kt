package mhelrigo.cocktailmanual.data.repository

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.ingredient.remote.IngredientApi
import mhelrigo.cocktailmanual.data.util.MockResponseFileReader
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

const val GET_DETAILS_PARAM = "Vodka"

class IngredientRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var ingredientApi: IngredientApi

    @Before
    fun init() {
        mockRetrofit()
        ingredientApi = retrofit.create(IngredientApi::class.java)
    }

    @Test
    fun getAll_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("GetAllIngredientMockResult.json"))))

        val result = ResultWrapper.build { ingredientApi.getAll() }

        assertEquals((result as ResultWrapper.Success).value.drinks.isNotEmpty(), true)
    }

    @Test
    fun getDetails_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("GetDetailsIngredientMockResult.json").content)))

        val result = ResultWrapper.build { ingredientApi.getDetails(GET_DETAILS_PARAM) }

        assertEquals(
            (result as ResultWrapper.Success).value.ingredients[0].strIngredient,
            GET_DETAILS_PARAM
        )
    }

    private fun mockRetrofit() {
        retrofit = MockRetrofit.let {
            it.mockWebServer = mockWebServer
            it.mockRetrofit()
        }
    }
}