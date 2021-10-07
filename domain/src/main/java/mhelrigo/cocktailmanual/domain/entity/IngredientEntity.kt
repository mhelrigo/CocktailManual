package mhelrigo.cocktailmanual.domain.entity

data class IngredientEntity(
    val idIngredient: String,
    val strABV: String,
    val strAlcohol: String,
    val strDescription: String,
    val strIngredient: String,
    val strIngredient1: String,
    val strType: String
) {
    fun thumbNail() : String = "https://www.thecocktaildb.com/images/ingredients/${strIngredient1?:strIngredient}-Medium.png"
}