package mhelrigo.cocktailmanual.data.repository

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.mhelrigo.commons.MockFileReader
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.drink.DrinkRepositoryImpl
import mhelrigo.cocktailmanual.data.repository.drink.local.DrinkDao
import mhelrigo.cocktailmanual.data.repository.drink.remote.DrinkApi
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.Retrofit

const val SEARCH_BY_FIRST_LETTER_PARAM = "a"
const val SEARCH_BY_NAME_PARAM = "Margarita"
const val SEARCH_BY_INGREDIENT = "Gin"
const val SEARCH_BY_TYPE = "Alcoholic"
const val SEARCH_BY_CATEGORY = "Ordinary_Drink"
const val SEARCH_BY_GLASS = "Cocktail_glass"
const val DRINK_ID = "11007"

class DrinkRepositoryTest {
    private lateinit var drinkRepositoryImpl: DrinkRepositoryImpl

    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var drinkApi: DrinkApi

    private lateinit var drinkDao: DrinkDao
    private lateinit var cockTailDatabase: CocktailDatabase

    @Before
    fun init() {
        mockRetrofit()
        mockRoom()
        drinkApi = retrofit.create(DrinkApi::class.java)
        drinkRepositoryImpl = DrinkRepositoryImpl(drinkApi, drinkDao, Dispatchers.IO)
    }

    @After
    fun clear() {
        cockTailDatabase.close()
    }

    @Test
    fun getRandomly_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("GetRandomlyMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> = drinkRepositoryImpl.getRandomly()) {
            is ResultWrapper.Success -> {
                assertEquals(result.value.drinks.isNotEmpty(), true)
            }
            is ResultWrapper.Error -> {
                assertThat(result.error, `isA`(Exception::class.java))
            }
        }
    }

    @Test
    fun getByPopularity_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("GetByPopularityMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> =
            drinkRepositoryImpl.getByPopularity()) {
            is ResultWrapper.Success -> {
                assertEquals(result.value.drinks.isNotEmpty(), true)
            }
            is ResultWrapper.Error -> {
                assertThat(result.error, `isA`(Exception::class.java))
            }
        }
    }

    @Test
    fun getLatest_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("GetLatestMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> = drinkRepositoryImpl.getLatest()) {
            is ResultWrapper.Success -> {
                assertEquals(result.value.drinks.isNotEmpty(), true)
            }
            is ResultWrapper.Error -> {
                assertThat(result.error, `isA`(Exception::class.java))
            }
        }
    }

    @Test
    fun searchByName_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("SearchByNameMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> =
            drinkRepositoryImpl.searchByName(SEARCH_BY_NAME_PARAM)) {
            is ResultWrapper.Success -> {
                assertEquals(result.value.drinks[0].strDrink, SEARCH_BY_NAME_PARAM)
            }
            is ResultWrapper.Error -> {
                assertThat(result.error, `isA`(Exception::class.java))
            }
        }
    }

    @Test
    fun searchByFirstLetter_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("SearchByFirstLetterMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> =
            drinkRepositoryImpl.searchByFirstLetter(SEARCH_BY_FIRST_LETTER_PARAM)) {
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

    @Test
    fun searchByIngredient_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("SearchByIngredientMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> =
            drinkRepositoryImpl.searchByIngredient(SEARCH_BY_INGREDIENT)) {
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

    @Test
    fun searchByDrinkType_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("SearchByDrinkTypeMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> =
            drinkRepositoryImpl.searchByDrinkType(SEARCH_BY_TYPE)) {
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

    @Test
    fun searchByCategory_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("SearchByCategoryMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> =
            drinkRepositoryImpl.searchByCategory(SEARCH_BY_CATEGORY)) {
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

    @Test
    fun searchByGlass_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("SearchByGlassMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> =
            drinkRepositoryImpl.searchByGlass(SEARCH_BY_GLASS)) {
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

    @Test
    fun getDetails_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("GetDetailsMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Drinks> =
            drinkRepositoryImpl.getDetails(DRINK_ID)) {
            is ResultWrapper.Success -> {
                assertEquals(
                    result.value.drinks[0].idDrink,
                    DRINK_ID
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

    private fun mockRoom() {
        val context = mock(Context::class.java)
        cockTailDatabase =
            Room.inMemoryDatabaseBuilder(context, CocktailDatabase::class.java).build()
        drinkDao = cockTailDatabase.drinkDao()
    }
}