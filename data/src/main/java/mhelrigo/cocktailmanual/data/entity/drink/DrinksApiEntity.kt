package mhelrigo.cocktailmanual.data.entity.drink

import com.google.gson.annotations.SerializedName
import mhelrigo.cocktailmanual.domain.entity.DrinkEntity

data class DrinksApiEntity (
    @SerializedName("drinks")
    val drinkEntities: List<DrinkEntity>
)