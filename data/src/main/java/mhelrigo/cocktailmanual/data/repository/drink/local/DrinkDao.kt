package mhelrigo.cocktailmanual.data.repository.drink.local

import androidx.room.*
import mhelrigo.cocktailmanual.data.entity.drink.DrinkDatabaseEntity

@Dao

interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drinkDatabaseEntity: DrinkDatabaseEntity)

    @Delete
    suspend fun delete(drinkDatabaseEntity: DrinkDatabaseEntity)

    @Query("SELECT * FROM DrinkDatabaseEntity WHERE idDrink = :idDrink")
    fun selectById(idDrink: String): DrinkDatabaseEntity

    @Query("SELECT * FROM DrinkDatabaseEntity")
    fun selectAllFavorites(): List<DrinkDatabaseEntity>
}