package mhelrigo.cocktailmanual.data.repository

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.glass.remote.GlassApi
import mhelrigo.cocktailmanual.data.util.MockResponseFileReader
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class GlassRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var glassApi: GlassApi

    @Before
    fun init() {
        mockRetrofit()
        glassApi = retrofit.create(GlassApi::class.java)
    }

    @Test
    fun getAll_Success() = runBlocking{
        mockWebServer.enqueue(MockResponse().setBody(MockResponseFileReader("GetAllGlassMockResult.json").content))

        val result = ResultWrapper.build { glassApi.getAll() }

        assertEquals((result as ResultWrapper.Success).value.drinks.isNotEmpty(), true)
    }

    private fun mockRetrofit() {
        retrofit = MockRetrofit.let {
            it.mockWebServer = mockWebServer
            it.mockRetrofit()
        }
    }
}