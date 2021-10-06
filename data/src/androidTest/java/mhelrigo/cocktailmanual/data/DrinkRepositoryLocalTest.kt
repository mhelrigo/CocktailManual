package mhelrigo.cocktailmanual.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.mhelrigo.commons.MockFileReader
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.mapper.DrinkEntityMapper
import mhelrigo.cocktailmanual.data.model.DrinkEntity
import mhelrigo.cocktailmanual.data.repository.CocktailDatabase
import mhelrigo.cocktailmanual.data.repository.drink.local.DrinkDao
import mhelrigo.cocktailmanual.domain.model.Drinks
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Had to separate this from local unit test since Room needs to run on device.
 * Basically what this does is it test all database operation for DrinkRepository
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DrinkRepositoryLocalTest {
    private lateinit var drinkDao: DrinkDao
    private lateinit var cockTailDatabase: CocktailDatabase

    private lateinit var drinkEntityMapper: DrinkEntityMapper

    @Before
    fun init() {
        mockRoom()
        drinkEntityMapper =  DrinkEntityMapper()
    }

    @After
    fun clear() {
        cockTailDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun addAndSelectFavoriteById() = runBlocking {
        val rawData = MockFileReader("SearchByNameMockResult.json").content
        val drinks: Drinks = Gson().fromJson(rawData, Drinks::class.java)

        drinkDao.insert(
            drinkEntityMapper.transform(drinks.drinks[0])
        )

        assertEquals(drinks.drinks[0].idDrink, drinkEntityMapper.transform(drinkDao.selectById(drinks.drinks[0].idDrink!!)).idDrink)
    }

    @Test
    @Throws(Exception::class)
    fun deleteFavoriteById() = runBlocking {
        val rawData = MockFileReader("SearchByNameMockResult.json").content
        val drinks: Drinks = Gson().fromJson(rawData, Drinks::class.java)

        drinkDao.insert(
            drinkEntityMapper.transform(drinks.drinks[0])
        )

        drinkDao.delete(
            drinkEntityMapper.transform(drinks.drinks[0])
        )

        assertEquals(null, drinkDao.selectById(drinks.drinks[0].idDrink!!))
    }

    private fun mockRoom() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        cockTailDatabase =
            Room.inMemoryDatabaseBuilder(context, CocktailDatabase::class.java).build()
        drinkDao = cockTailDatabase.drinkDao()
    }
}