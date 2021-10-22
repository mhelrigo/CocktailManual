package com.mhelrigo.cocktailmanual.model

import com.mhelrigo.cocktailmanual.R
import timber.log.Timber

/**
 * Presentation version of [mhelrigo.cocktailmanual.domain.entity.DrinkEntity]
 * */
data class DrinkModel(
    val dateModified: String?,
    val idDrink: String?,
    val strAlcoholic: String?,
    val strCategory: String?,
    val strCreativeCommonsConfirmed: String?,
    val strDrink: String?,
    val strDrinkAlternate: String?,
    val strDrinkThumb: String?,
    val strGlass: String?,
    val strIBA: String?,
    val strImageAttribution: String?,
    val strImageSource: String?,
    val strIngredient1: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strInstructions: String?,
    val strInstructionsDE: String?,
    val strInstructionsES: String?,
    val strInstructionsFR: String?,
    val strInstructionsIT: String?,
    val strInstructionsZH_HANS: String?,
    val strInstructionsZH_HANT: String?,
    val strMeasure1: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strTags: String?,
    val strVideo: String?,
    var isFavourite: Boolean,
    var backGroundColorDrawableColor: Int,
    var bindingAdapterPosition: Int,
    var drinkCollectionType: DrinkCollectionType,
    var updateAt: String?
) {
    companion object Factory {
        const val VIEW_HOLDER_SMALL = 0
        const val VIEW_HOLDER_REGULAR = 1
        const val VIEW_HOLDER_BIG = 2

        /**
         * This will assign where the list of Drinks came from see @[DrinkCollectionType]
         *
         * @param [toBeAssigned] the list to be iterated so that the items are assigned a [DrinkCollectionType]
         * @param [collectionType] the [DrinkCollectionType] to be assigned to the list
         * */
        fun assignCollectionType(
            toBeAssigned: List<DrinkModel>,
            collectionType: DrinkCollectionType
        ): List<DrinkModel> {
            return toBeAssigned.apply {
                map {
                    it.drinkCollectionType = collectionType
                }
            }
        }
    }

    fun markFavorite(a: Boolean) {
        this.isFavourite = a
    }

    fun returnIconForFavorite(): Int {
        return if (isFavourite) {
            R.drawable.ic_favorite_red
        } else {
            R.drawable.ic_favorite_filled
        }
    }

    fun returnViewHolderType(): Int {
        return when (drinkCollectionType) {
            DrinkCollectionType.LATEST -> {
                VIEW_HOLDER_REGULAR
            }
            DrinkCollectionType.POPULAR -> {
                VIEW_HOLDER_REGULAR
            }
            DrinkCollectionType.RANDOM, DrinkCollectionType.FAVORITE -> {
                VIEW_HOLDER_BIG
            }
            DrinkCollectionType.FILTERED_BY_INGREDIENTS, DrinkCollectionType.SEARCH_BY_NAME -> {
                VIEW_HOLDER_SMALL
            }
            else -> throw IllegalArgumentException()
        }
    }

    fun returnIngredientWithMeasurement(): String {
        return (if (strMeasure1.isNullOrBlank()) "" else "$strMeasure1") + (if (strIngredient1.isNullOrBlank()) "" else "$strIngredient1\n") +
                (if (strMeasure2.isNullOrBlank()) "" else "$strMeasure2") + (if (strIngredient2.isNullOrBlank()) "" else "$strIngredient2\n") +
                (if (strMeasure3.isNullOrBlank()) "" else "$strMeasure3") + (if (strIngredient3.isNullOrBlank()) "" else "$strIngredient3\n") +
                (if (strMeasure4.isNullOrBlank()) "" else "$strMeasure4") + (if (strIngredient4.isNullOrBlank()) "" else "$strIngredient4\n") +
                (if (strMeasure5.isNullOrBlank()) "" else "$strMeasure5") + (if (strIngredient5.isNullOrBlank()) "" else "$strIngredient5\n") +
                (if (strMeasure6.isNullOrBlank()) "" else "$strMeasure6") + (if (strIngredient6.isNullOrBlank()) "" else "$strIngredient6\n") +
                (if (strMeasure7.isNullOrBlank()) "" else "$strMeasure7") + (if (strIngredient7.isNullOrBlank()) "" else "$strIngredient7\n") +
                (if (strMeasure8.isNullOrBlank()) "" else "$strMeasure8") + (if (strIngredient8.isNullOrBlank()) "" else "$strIngredient8\n") +
                (if (strMeasure9.isNullOrBlank()) "" else "$strMeasure9") + (if (strIngredient9.isNullOrBlank()) "" else "$strIngredient9\n") +
                if (strMeasure10.isNullOrBlank()) "" else "$strMeasure10" + if (strIngredient10.isNullOrBlank()) "" else "$strIngredient10"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DrinkModel) {
            return false
        }

        return idDrink.equals(other.idDrink) && isFavourite == other.isFavourite
    }
}