package com.mhelrigo.domain.usecase.glass

import com.mhelrigo.domain.TestData
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.domain.repository.GlassRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.glass.GetAllGlassUseCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllGlassTest {
    @Mock
    private lateinit var glassRepository: GlassRepository

    @Test
    fun getAllGlass_Success() = runBlocking {
        val data = TestData.provideGlasses()

        `when`(glassRepository.getAll()).thenReturn(ResultWrapper.build { data })

        val getAllGlassUseCase = GetAllGlassUseCase(glassRepository)

        val result = getAllGlassUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Success).value, `is`(data))
    }

    @Test
    fun getAllGlass_Error() = runBlocking {
        `when`(glassRepository.getAll()).thenReturn(ResultWrapper.build { throw Exception("Any kind of Exception") })

        val getAllGlassUseCase = GetAllGlassUseCase(glassRepository)

        val result = getAllGlassUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Error).error, `is`(java.lang.Exception::class.java))
    }
}