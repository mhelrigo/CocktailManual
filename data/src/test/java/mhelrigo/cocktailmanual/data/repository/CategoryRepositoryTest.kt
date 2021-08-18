package mhelrigo.cocktailmanual.data.repository

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.category.remote.CategoryApi
import mhelrigo.cocktailmanual.data.util.MockResponseFileReader
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class CategoryRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var categoryApi: CategoryApi

    @Before
    fun init() {
        mockRetrofit()
        categoryApi = retrofit.create(CategoryApi::class.java)
    }

    @Test
    fun getAll_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockResponseFileReader("GetAllCategoryMockResult.json").content)))

        val result = ResultWrapper.build { categoryApi.getAll() }

        assertEquals((result as ResultWrapper.Success).value.drinks.isNotEmpty(), true)
    }

    private fun mockRetrofit() {
        retrofit = MockRetrofit.let {
            it.mockWebServer = mockWebServer
            it.mockRetrofit()
        }
    }
}