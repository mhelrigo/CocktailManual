package com.mhelrigo.cocktailmanual.mapper

import com.mhelrigo.cocktailmanual.model.DrinkCollectionType
import com.mhelrigo.cocktailmanual.model.DrinkModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrinkModelMapper @Inject constructor() {
    /**
     * Transform a [DrinkModel] from domain into presentation [DrinkModel]
     *
     * @param [toBeConverted] the list to be processed
     * */
    private fun transform(drinkEntity: mhelrigo.cocktailmanual.domain.entity.DrinkEntity): DrinkModel {
        return DrinkModel(
            dateModified = drinkEntity.dateModified,
            idDrink = drinkEntity.idDrink,
            strAlcoholic = drinkEntity.strAlcoholic,
            strCategory = drinkEntity.strCategory,
            strCreativeCommonsConfirmed = drinkEntity.strCreativeCommonsConfirmed,
            strDrink = drinkEntity.strDrink,
            strDrinkAlternate = drinkEntity.strDrinkAlternate,
            strDrinkThumb = drinkEntity.strDrinkThumb,
            strGlass = drinkEntity.strGlass,
            strIBA = drinkEntity.strIBA,
            strImageAttribution = drinkEntity.strImageAttribution,
            strImageSource = drinkEntity.strImageSource,
            strIngredient1 = drinkEntity.strIngredient1,
            strIngredient10 = drinkEntity.strIngredient10,
            strIngredient11 = drinkEntity.strIngredient11,
            strIngredient12 = drinkEntity.strIngredient12,
            strIngredient13 = drinkEntity.strIngredient13,
            strIngredient14 = drinkEntity.strIngredient14,
            strIngredient15 = drinkEntity.strIngredient15,
            strIngredient2 = drinkEntity.strIngredient2,
            strIngredient3 = drinkEntity.strIngredient3,
            strIngredient4 = drinkEntity.strIngredient4,
            strIngredient5 = drinkEntity.strIngredient5,
            strIngredient6 = drinkEntity.strIngredient6,
            strIngredient7 = drinkEntity.strIngredient7,
            strIngredient8 = drinkEntity.strIngredient8,
            strIngredient9 = drinkEntity.strIngredient9,
            strInstructions = drinkEntity.strInstructions,
            strInstructionsDE = drinkEntity.strInstructionsDE,
            strInstructionsES = drinkEntity.strInstructionsES,
            strInstructionsFR = drinkEntity.strInstructionsFR,
            strInstructionsIT = drinkEntity.strInstructionsIT,
            strInstructionsZH_HANS = drinkEntity.strInstructionsZH_HANS,
            strInstructionsZH_HANT = drinkEntity.strInstructionsZH_HANT,
            strMeasure1 = drinkEntity.strMeasure1,
            strMeasure10 = drinkEntity.strMeasure10,
            strMeasure11 = drinkEntity.strMeasure11,
            strMeasure12 = drinkEntity.strMeasure12,
            strMeasure13 = drinkEntity.strMeasure13,
            strMeasure14 = drinkEntity.strMeasure14,
            strMeasure15 = drinkEntity.strMeasure15,
            strMeasure2 = drinkEntity.strMeasure2,
            strMeasure3 = drinkEntity.strMeasure3,
            strMeasure4 = drinkEntity.strMeasure4,
            strMeasure5 = drinkEntity.strMeasure5,
            strMeasure6 = drinkEntity.strMeasure6,
            strMeasure7 = drinkEntity.strMeasure7,
            strMeasure8 = drinkEntity.strMeasure8,
            strMeasure9 = drinkEntity.strMeasure9,
            strTags = drinkEntity.strTags,
            strVideo = drinkEntity.strVideo,
            isFavourite = drinkEntity.isFavourite,
            bindingAdapterPosition = -1,
            backGroundColorDrawableColor = 0,
            drinkCollectionType = DrinkCollectionType.NONE,
            updateAt = System.currentTimeMillis().toString()
        )
    }

    fun transform(drinkModel: DrinkModel): mhelrigo.cocktailmanual.domain.entity.DrinkEntity =
        mhelrigo.cocktailmanual.domain.entity.DrinkEntity(
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
            drinkModel.strMeasure2,
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
    fun transform(toBeConverted: List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>): List<DrinkModel> {
        return toBeConverted.run {
            map {
                transform(it)
            }
        }
    }
}