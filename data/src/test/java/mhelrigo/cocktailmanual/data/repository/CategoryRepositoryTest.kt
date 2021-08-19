package mhelrigo.cocktailmanual.data.repository

import com.google.gson.Gson
import com.mhelrigo.commons.MockFileReader
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.category.CategoryRepositoryImpl
import mhelrigo.cocktailmanual.data.repository.category.remote.CategoryApi
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.model.Categories
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class CategoryRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var categoryApi: CategoryApi
    private lateinit var categoryRepository: CategoryRepositoryImpl

    @Before
    fun init() {
        mockRetrofit()
        categoryApi = retrofit.create(CategoryApi::class.java)
        categoryRepository = CategoryRepositoryImpl(categoryApi, Dispatchers.IO)
    }

    @Test
    fun getAll_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(MockFileReader("GetAllCategoryMockResult.json").content)))

        when (val result: ResultWrapper<Exception, Categories> = categoryRepository.getAll()) {
            is ResultWrapper.Error -> {
                assertThat(result.error, CoreMatchers.isA(Exception::class.java))
            }
            is ResultWrapper.Success -> {
                assertEquals(result.value.drinks.isNotEmpty(), true)
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