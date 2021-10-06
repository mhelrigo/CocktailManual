package mhelrigo.cocktailmanual.data.repository.drink.local

import androidx.room.*
import mhelrigo.cocktailmanual.data.model.DrinkEntity

@Dao
interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drinkEntity: DrinkEntity)

    @Delete
    suspend fun delete(drinkEntity: DrinkEntity)

    @Query("SELECT * FROM DrinkEntity WHERE idDrink = :idDrink")
    fun selectById(idDrink: String): DrinkEntity

    @Query("SELECT * FROM DrinkEntity")
    fun selectAllFavorites(): List<DrinkEntity>
}