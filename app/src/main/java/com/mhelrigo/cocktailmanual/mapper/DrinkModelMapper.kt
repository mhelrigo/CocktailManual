package com.mhelrigo.cocktailmanual.mapper

import com.mhelrigo.cocktailmanual.model.DrinkCollectionType
import com.mhelrigo.cocktailmanual.model.DrinkModel
import mhelrigo.cocktailmanual.domain.model.Drink
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrinkModelMapper @Inject constructor() {
    /**
     * Transform a [DrinkModel] from domain into presentation [DrinkModel]
     *
     * @param [toBeConverted] the list to be processed
     * */
    private fun transform(drink: mhelrigo.cocktailmanual.domain.model.Drink): DrinkModel {
        return DrinkModel(
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
            drinkCollectionType = DrinkCollectionType.NONE
        )
    }

    fun transform(drinkModel: DrinkModel): mhelrigo.cocktailmanual.domain.model.Drink =
        mhelrigo.cocktailmanual.domain.model.Drink(
            drinkModel.dateModified,
            drinkModel.idDrink,
            drinkModel.strAlcoholic,
            drinkModel.strCategory,
            drinkModel.strCreativeCommonsConfirmed,
            drinkModel.strDrink,
            drinkModel.strDrinkAlternate,
            drinkModel.strDrinkThumb,
            drinkModel.strGlass,
            drinkModel.strIBA,
            drinkModel.strImageAttribution,
            drinkModel.strImageSource,
            drinkModel.strIngredient1,
            drinkModel.strIngredient10,
            drinkModel.strIngredient11,
            drinkModel.strIngredient12,
            drinkModel.strIngredient13,
            drinkModel.strIngredient14,
            drinkModel.strIngredient15,
            drinkModel.strIngredient2,
            drinkModel.strIngredient3,
            drinkModel.strIngredient4,
            drinkModel.strIngredient5,
            drinkModel.strIngredient6,
            drinkModel.strIngredient7,
            drinkModel.strIngredient8,
            drinkModel.strIngredient9,
            drinkModel.strInstructions,
            drinkModel.strInstructionsDE,
            drinkModel.strInstructionsES,
            drinkModel.strInstructionsFR,
            drinkModel.strInstructionsIT,
            drinkModel.strInstructionsZH_HANS,
            drinkModel.strInstructionsZH_HANT,
            drinkModel.strMeasure1,
            drinkModel.strMeasure10,
            drinkModel.strMeasure11,
            drinkModel.strMeasure12,
            drinkModel.strMeasure13,
            drinkModel.strMeasure14,
            drinkModel.strMeasure15,
            drinkModel. strMeasure2,
            drinkModel.strMeasure3,
            drinkModel.strMeasure4,
            drinkModel.strMeasure5,
            drinkModel.strMeasure6,
            drinkModel.strMeasure7,
            drinkModel.strMeasure8,
            drinkModel.strMeasure9,
            drinkModel.strTags,
            drinkModel.strVideo,
            drinkModel.isFavourite
        )

    /**
     * Transform a collection of [DrinkModel] from domain into a collection of presentation [DrinkModel]
     *
     * @param [toBeConverted] the list to be processed
     * */
    fun transform(toBeConverted: List<mhelrigo.cocktailmanual.domain.model.Drink>): List<DrinkModel> {
        return toBeConverted.run {
            map {
                transform(it)
            }
        }
    }
}