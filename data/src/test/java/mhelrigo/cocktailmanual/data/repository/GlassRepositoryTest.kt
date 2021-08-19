package mhelrigo.cocktailmanual.data.repository

import com.mhelrigo.commons.MockFileReader
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.repository.glass.GlassRepositoryImpl
import mhelrigo.cocktailmanual.data.repository.glass.remote.GlassApi
import mhelrigo.cocktailmanual.data.util.MockRetrofit
import mhelrigo.cocktailmanual.domain.model.Glasses
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class GlassRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var glassRepositoryImpl: GlassRepositoryImpl

    @Before
    fun init() {
        mockRetrofit()
        glassRepositoryImpl =
            GlassRepositoryImpl(retrofit.create(GlassApi::class.java), Dispatchers.Unconfined)
    }

    @Test
    fun getAll_Success() = runBlocking {
        mockWebServer.enqueue(MockResponse().setBody(MockFileReader("GetAllGlassMockResult.json").content))

        when (val result: ResultWrapper<Exception, Glasses> = glassRepositoryImpl.getAll()) {
            is ResultWrapper.Success -> {
                assertEquals(result.value.drinks.isNotEmpty(), true)
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