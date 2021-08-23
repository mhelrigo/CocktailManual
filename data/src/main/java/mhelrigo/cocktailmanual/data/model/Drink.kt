package mhelrigo.cocktailmanual.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drink(@PrimaryKey val uid: Int, val idDrink: Int) {
    /**
     * idDrink is the only field needed for now as we will get the rest of the data from API result.
     * */
    fun toDrinkUseCase(): mhelrigo.cocktailmanual.domain.model.Drink {
        return mhelrigo.cocktailmanual.domain.model.Drink(
            "",
            idDrink = idDrink.toString(),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            true
        )
    }
}