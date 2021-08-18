package mhelrigo.cocktailmanual.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drink(@PrimaryKey val uid: Int, val idDrink: String) {
}