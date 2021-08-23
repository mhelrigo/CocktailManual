package mhelrigo.cocktailmanual.data.repository.drink.local

import androidx.room.*
import mhelrigo.cocktailmanual.data.model.Drink

@Dao
interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drink: Drink)

    @Delete
    fun delete(drink: Drink)

    @Query("SELECT * FROM Drink WHERE idDrink = :idDrink")
    fun selectById(idDrink: String): Drink

    @Query("SELECT * FROM Drink")
    fun selectAllFavorites(): List<Drink>
}