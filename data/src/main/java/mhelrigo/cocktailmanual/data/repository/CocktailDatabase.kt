package mhelrigo.cocktailmanual.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import mhelrigo.cocktailmanual.data.model.DrinkEntity
import mhelrigo.cocktailmanual.data.repository.drink.local.DrinkDao

@Database(entities = [DrinkEntity::class], version = 1, exportSchema = false)
abstract class CocktailDatabase : RoomDatabase() {
    companion object Factory {
        const val name = "cocktail_db"
    }

    abstract fun drinkDao(): DrinkDao
}