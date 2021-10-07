package mhelrigo.cocktailmanual.data.entity.ingredient

import com.google.gson.annotations.SerializedName
import mhelrigo.cocktailmanual.domain.entity.IngredientEntity

data class IngredientsApiEntity(
    @SerializedName(value = "ingredients", alternate = ["drinks"])
    val ingredientEntity: List<IngredientEntity>,
)