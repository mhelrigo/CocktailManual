package com.mhelrigo.domain.usecase.category

import com.mhelrigo.domain.TestData
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.domain.repository.CategoryRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.category.GetAllCategoryUseCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllCategoryTest {
    @Mock
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun getAllCategory_Success() = runBlocking {
        val data = TestData.provideCategories()

        `when`(categoryRepository.getAll()).thenReturn(ResultWrapper.build { data })

        val getAllCategoryUseCase = GetAllCategoryUseCase(categoryRepository)

        val result = getAllCategoryUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Success).value, `is`(data))
    }

    @Test
    fun getAllCategory_Error() = runBlocking {
        `when`(categoryRepository.getAll()).thenReturn(ResultWrapper.build { throw Exception("Might be anything exception") })

        val getAllCategoryUseCase = GetAllCategoryUseCase(categoryRepository)

        val result = getAllCategoryUseCase.buildExecutable(null)

        assertThat((result as ResultWrapper.Error).error, `isA`(java.lang.Exception::class.java))
    }
}