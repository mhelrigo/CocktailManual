package mhelrigo.cocktailmanual.data.repository.drink.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import mhelrigo.cocktailmanual.data.model.Drink

@Dao
interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drink: Drink)

    @Delete
    fun delete(drink: Drink)
}