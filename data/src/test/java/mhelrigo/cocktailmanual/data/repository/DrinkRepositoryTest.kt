package mhelrigo.cocktailmanual.data.repository

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.drink.remote.DrinkApi
import mhelrigo.cocktailmanual.data.util.MockResponseFileReader
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_BY_FIRST_LETTER_PARAM = "a"
const val SEARCH_BY_NAME_PARAM = "Margarita"
const val SEARCH_BY_INGREDIENT = "Gin"
const val SEARCH_BY_TYPE = "Alcoholic"
const val SEARCH_BY_CATEGORY = "Ordinary_Drink"
const val SEARCH_BY_GLASS = "Cocktail_glass"
const val DRINK_ID = "11007"

class DrinkRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var drinkApi: DrinkApi

    @Before
    fun init() {
        mockRetrofit()
        drinkApi = retrofit.create(DrinkApi::class.java)
    }

    @Test
    fun getRandomly_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("GetRandomlyMockResult.json").content)))

        val result = ResultWrapper.build { drinkApi.getRandomly() }

        assertEquals((result as ResultWrapper.Success).value.drinks.isNotEmpty(), true)
    }

    @Test
    fun getByPopularity_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("GetByPopularityMockResult.json").content)))

        val result = ResultWrapper.build { drinkApi.getByPopularity() }

        assertEquals((result as ResultWrapper.Success).value.drinks.isNotEmpty(), true)
    }

    @Test
    fun getLatest_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("GetLatestMockResult.json").content)))

        val result = ResultWrapper.build { drinkApi.getLatest() }

        assertEquals((result as ResultWrapper.Success).value.drinks.isNotEmpty(), true)
    }

    @Test
    fun searchByName_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("SearchByNameMockResult.json").content)))

        val result = ResultWrapper.build { drinkApi.searchByName(SEARCH_BY_NAME_PARAM) }

        assertEquals(
            (result as ResultWrapper.Success).value.drinks[0].strDrink,
            SEARCH_BY_NAME_PARAM
        )
    }

    @Test
    fun searchByFirstLetter_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("SearchByFirstLetterMockResult.json").content)))

        val result =
            ResultWrapper.build { drinkApi.searchByFirstLetter(SEARCH_BY_FIRST_LETTER_PARAM) }

        assertEquals(
            (result as ResultWrapper.Success).value.drinks.isNotEmpty(),
            true
        )
    }

    @Test
    fun searchByIngredient_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("SearchByIngredientMockResult.json").content)))

        val result =
            ResultWrapper.build { drinkApi.searchByIngredient(SEARCH_BY_INGREDIENT) }

        assertEquals(
            (result as ResultWrapper.Success).value.drinks.isNotEmpty(),
            true
        )
    }

    @Test
    fun searchByDrinkType_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("SearchByDrinkTypeMockResult.json").content)))

        val result =
            ResultWrapper.build { drinkApi.searchByDrinkType(SEARCH_BY_TYPE) }

        assertEquals(
            (result as ResultWrapper.Success).value.drinks.isNotEmpty(),
            true
        )
    }

    @Test
    fun searchByCategory_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("SearchByCategoryMockResult.json").content)))

        val result =
            ResultWrapper.build { drinkApi.searchByCategory(SEARCH_BY_CATEGORY) }

        assertEquals(
            (result as ResultWrapper.Success).value.drinks.isNotEmpty(),
            true
        )
    }

    @Test
    fun searchByGlass_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("SearchByGlassMockResult.json").content)))

        val result =
            ResultWrapper.build { drinkApi.searchByGlass(SEARCH_BY_GLASS) }

        assertEquals(
            (result as ResultWrapper.Success).value.drinks.isNotEmpty(),
            true
        )
    }

    @Test
    fun getDetails_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("GetDetailsMockResult.json").content)))

        val result =
            ResultWrapper.build { drinkApi.getDetails(DRINK_ID) }

        assertEquals(
            (result as ResultWrapper.Success).value.drinks[0].idDrink,
            DRINK_ID
        )
    }

    private fun mockRetrofit() {
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mockWebServer.url("https://www.thecocktaildb.com/api/json/v2/9973533/"))
            .build()
    }
}