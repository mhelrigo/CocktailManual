package mhelrigo.cocktailmanual.data.repository

import com.google.gson.Gson
import com.mhelrigo.commons.MockFileReader
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.drinktype.DrinkTypeRepositoryImpl
import mhelrigo.cocktailmanual.data.repository.drinktype.remote.DrinkTypeApi
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.model.DrinkTypes
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class DrinkTypeRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var drinkTypeRepositoryImpl: DrinkTypeRepositoryImpl

    @Before
    fun init() {
        mockRetrofit()
        drinkTypeRepositoryImpl = DrinkTypeRepositoryImpl(
            retrofit.create(DrinkTypeApi::class.java),
            Dispatchers.Unconfined
        )
    }

    @Test
    fun getAll_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("GetAllDrinkTypeMockResult.json").content)))

        when (val result: ResultWrapper<Exception, DrinkTypes> = drinkTypeRepositoryImpl.getAll()) {
            is ResultWrapper.Success -> {
                assertEquals(
                    result.value.drinks.isNotEmpty(),
                    true
                )
            }
            is ResultWrapper.Error -> {
                assertThat(result.error, `isA`(Exception::class.java))
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