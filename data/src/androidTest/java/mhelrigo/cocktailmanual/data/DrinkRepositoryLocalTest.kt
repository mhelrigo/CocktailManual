package mhelrigo.cocktailmanual.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.mhelrigo.commons.MockFileReader
import kotlinx.coroutines.runBlocking
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

    @Before
    fun init() {
        mockRoom()
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
            mhelrigo.cocktailmanual.data.model.Drink(
                drinks.drinks[0].idDrink?.toInt()!!,
                drinks.drinks[0].idDrink as Int
            )
        )

        assertEquals(drinks.drinks[0].idDrink, drinkDao.selectById(drinks.drinks[0].idDrink!!).toDrinkUseCase().idDrink)
    }

    @Test
    @Throws(Exception::class)
    fun deleteFavoriteById() = runBlocking {
        val rawData = MockFileReader("SearchByNameMockResult.json").content
        val drinks: Drinks = Gson().fromJson(rawData, Drinks::class.java)

        drinkDao.insert(
            mhelrigo.cocktailmanual.data.model.Drink(
                drinks.drinks[0].idDrink!!.toInt(),
                drinks.drinks[0].idDrink as Int
            )
        )

        drinkDao.delete(
            mhelrigo.cocktailmanual.data.model.Drink(
                drinks.drinks[0].idDrink!!.toInt(),
                drinks.drinks[0].idDrink as Int
            )
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