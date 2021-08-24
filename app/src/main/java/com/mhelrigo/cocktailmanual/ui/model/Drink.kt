package com.mhelrigo.cocktailmanual.ui.model

import com.mhelrigo.cocktailmanual.R

enum class FromCollectionOf {
    LATEST,
    POPULAR,
    RANDOM,
    NONE
}

data class Drink(
    val dateModified: String?,
    val idDrink: String?,
    val strAlcoholic: String?,
    val strCategory: String?,
    val strCreativeCommonsConfirmed: String?,
    val strDrink: String?,
    val strDrinkAlternate: Any?,
    val strDrinkThumb: String?,
    val strGlass: String?,
    val strIBA: String?,
    val strImageAttribution: String?,
    val strImageSource: String?,
    val strIngredient1: String?,
    val strIngredient10: Any?,
    val strIngredient11: Any?,
    val strIngredient12: Any?,
    val strIngredient13: Any?,
    val strIngredient14: Any?,
    val strIngredient15: Any?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: Any?,
    val strIngredient6: Any?,
    val strIngredient7: Any?,
    val strIngredient8: Any?,
    val strIngredient9: Any?,
    val strInstructions: String?,
    val strInstructionsDE: String?,
    val strInstructionsES: Any?,
    val strInstructionsFR: Any?,
    val strInstructionsIT: String?,
    val strInstructionsZH_HANS: String?,
    val strInstructionsZH_HANT: String?,
    val strMeasure1: String?,
    val strMeasure10: Any?,
    val strMeasure11: Any?,
    val strMeasure12: Any?,
    val strMeasure13: Any?,
    val strMeasure14: Any?,
    val strMeasure15: Any?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: Any?,
    val strMeasure5: Any?,
    val strMeasure6: Any?,
    val strMeasure7: Any?,
    val strMeasure8: Any?,
    val strMeasure9: Any?,
    val strTags: String?,
    val strVideo: Any?,
    var isFavourite: Boolean,
    var backGroundColorDrawableColor: Int,
    var bindingAdapterPosition: Int,
    var fromCollectionOf: FromCollectionOf
) {
    companion object Factory {
        fun fromDrinkDomainModel(drink: mhelrigo.cocktailmanual.domain.model.Drink): Drink {
            return Drink(
                dateModified = drink.dateModified,
                idDrink = drink.idDrink,
                strAlcoholic = drink.strAlcoholic,
                strCategory = drink.strCategory,
                strCreativeCommonsConfirmed = drink.strCreativeCommonsConfirmed,
                strDrink = drink.strDrink,
                strDrinkAlternate = drink.strDrinkAlternate,
                strDrinkThumb = drink.strDrinkThumb,
                strGlass = drink.strGlass,
                strIBA = drink.strIBA,
                strImageAttribution = drink.strImageAttribution,
                strImageSource = drink.strImageSource,
                strIngredient1 = drink.strIngredient1,
                strIngredient10 = drink.strIngredient10,
                strIngredient11 = drink.strIngredient11,
                strIngredient12 = drink.strIngredient12,
                strIngredient13 = drink.strIngredient13,
                strIngredient14 = drink.strIngredient14,
                strIngredient15 = drink.strIngredient15,
                strIngredient2 = drink.strIngredient2,
                strIngredient3 = drink.strIngredient3,
                strIngredient4 = drink.strIngredient4,
                strIngredient5 = drink.strIngredient5,
                strIngredient6 = drink.strIngredient6,
                strIngredient7 = drink.strIngredient7,
                strIngredient8 = drink.strIngredient8,
                strIngredient9 = drink.strIngredient9,
                strInstructions = drink.strInstructions,
                strInstructionsDE = drink.strInstructionsDE,
                strInstructionsES = drink.strInstructionsES,
                strInstructionsFR = drink.strInstructionsFR,
                strInstructionsIT = drink.strInstructionsIT,
                strInstructionsZH_HANS = drink.strInstructionsZH_HANS,
                strInstructionsZH_HANT = drink.strInstructionsZH_HANT,
                strMeasure1 = drink.strMeasure1,
                strMeasure10 = drink.strMeasure10,
                strMeasure11 = drink.strMeasure11,
                strMeasure12 = drink.strMeasure12,
                strMeasure13 = drink.strMeasure13,
                strMeasure14 = drink.strMeasure14,
                strMeasure15 = drink.strMeasure15,
                strMeasure2 = drink.strMeasure2,
                strMeasure3 = drink.strMeasure3,
                strMeasure4 = drink.strMeasure4,
                strMeasure5 = drink.strMeasure5,
                strMeasure6 = drink.strMeasure6,
                strMeasure7 = drink.strMeasure7,
                strMeasure8 = drink.strMeasure8,
                strMeasure9 = drink.strMeasure9,
                strTags = drink.strTags,
                strVideo = drink.strVideo,
                isFavourite = drink.isFavourite,
                bindingAdapterPosition = -1,
                backGroundColorDrawableColor = 0,
                fromCollectionOf = FromCollectionOf.NONE
            )
        }
    }

    fun markFavorite(a: Boolean) {
        this.isFavourite = a
    }

    fun returnIconForFavorite(): Int {
        return if (isFavourite) {
            R.drawable.ic_favorite_red
        } else {
            R.drawable.ic_favorite_border_black
        }
    }

    fun assignDrawableColor() {
        backGroundColorDrawableColor = when ((1..6).random()) {
            1 -> {
                R.color.red_100
            }
            2 -> {
                R.color.pink_100
            }
            3 -> {
                R.color.purple_100
            }
            4 -> {
                R.color.yellow_50
            }
            5 -> {
                R.color.amber_50
            }
            6 -> {
                R.color.orange_50
            }
            else -> R.color.red_100
        }
    }

    fun returnMeasurements(): String {
        return (if (strMeasure1.isNullOrBlank()) "" else " $strMeasure1") +
                (if (strMeasure2.isNullOrBlank()) "" else "\n $strMeasure2") +
                (if (strMeasure3.isNullOrBlank()) "" else "\n $strMeasure3") +
                (if (strMeasure4 == null) "" else "\n $strMeasure4") +
                (if (strMeasure5 == null) "" else "\n $strMeasure5") +
                (if (strMeasure6 == null) "" else "\n $strMeasure6") +
                (if (strMeasure7 == null) "" else "\n $strMeasure7") +
                (if (strMeasure8 == null) "" else "\n $strMeasure8") +
                (if (strMeasure9 == null) "" else "\n $strMeasure9") +
                if (strMeasure10 == null) "" else "\n $strMeasure10"
    }

    fun returnIngredients(): String {
        return (if (strIngredient1.isNullOrBlank()) "" else "$strIngredient1") +
                (if (strIngredient2.isNullOrBlank()) "" else "\n $strIngredient2") +
                (if (strIngredient3.isNullOrBlank()) "" else "\n $strIngredient3") +
                (if (strIngredient4.isNullOrBlank()) "" else "\n $strIngredient4") +
                (if (strIngredient5 == null) "" else "\n $strIngredient5") +
                (if (strIngredient6 == null) "" else "\n $strIngredient6") +
                (if (strIngredient7 == null) "" else "\n $strIngredient7") +
                (if (strIngredient8 == null) "" else "\n $strIngredient8") +
                (if (strIngredient9 == null) "" else "\n $strIngredient9") +
                if (strIngredient10 == null) "" else "\n $strIngredient10"
    }

    fun toDrinkDomainModel(): mhelrigo.cocktailmanual.domain.model.Drink =
        mhelrigo.cocktailmanual.domain.model.Drink(
            dateModified,
            idDrink,
            strAlcoholic,
            strCategory,
            strCreativeCommonsConfirmed,
            strDrink,
            strDrinkAlternate,
            strDrinkThumb,
            strGlass,
            strIBA,
            strImageAttribution,
            strImageSource,
            strIngredient1,
            strIngredient10,
            strIngredient11,
            strIngredient12,
            strIngredient13,
            strIngredient14,
            strIngredient15,
            strIngredient2,
            strIngredient3,
            strIngredient4,
            strIngredient5,
            strIngredient6,
            strIngredient7,
            strIngredient8,
            strIngredient9,
            strInstructions,
            strInstructionsDE,
            strInstructionsES,
            strInstructionsFR,
            strInstructionsIT,
            strInstructionsZH_HANS,
            strInstructionsZH_HANT,
            strMeasure1,
            strMeasure10,
            strMeasure11,
            strMeasure12,
            strMeasure13,
            strMeasure14,
            strMeasure15,
            strMeasure2,
            strMeasure3,
            strMeasure4,
            strMeasure5,
            strMeasure6,
            strMeasure7,
            strMeasure8,
            strMeasure9,
            strTags,
            strVideo,
            isFavourite
        )
}