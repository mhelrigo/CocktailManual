package mhelrigo.cocktailmanual.data.repository

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.drinktype.remote.DrinkTypeApi
import mhelrigo.cocktailmanual.data.util.MockResponseFileReader
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class DrinkTypeRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var drinkTypeApi: DrinkTypeApi

    @Before
    fun init() {
        mockRetrofit()
        drinkTypeApi = retrofit.create(DrinkTypeApi::class.java)
    }

    @Test
    fun getAll_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("GetAllDrinkTypeMockResult.json").content)))

        val result = ResultWrapper.build { drinkTypeApi.getAll() }

        assertEquals((result as ResultWrapper.Success).value.drinks.isNotEmpty(), true)
    }

    private fun mockRetrofit() {
        retrofit = MockRetrofit.let {
            it.mockWebServer = mockWebServer
            it.mockRetrofit()
        }
    }
}