package mhelrigo.cocktailmanual.data.mapper

import mhelrigo.cocktailmanual.data.entity.drink.DrinkDatabaseEntity
import mhelrigo.cocktailmanual.data.entity.drink.DrinksApiEntity
import mhelrigo.cocktailmanual.domain.entity.DrinksEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class DrinkEntityMapper @Inject constructor() {

    fun transform(drinkEntity: mhelrigo.cocktailmanual.domain.entity.DrinkEntity): DrinkDatabaseEntity {
        return DrinkDatabaseEntity(
            dateModified = drinkEntity.dateModified,
            idDrink = drinkEntity.idDrink?.toInt()!!,
            uid = drinkEntity.idDrink?.toInt()!!,
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
            isFavourite = drinkEntity.isFavourite
        )
    }

    fun transform(drinkDatabaseEntity: DrinkDatabaseEntity): mhelrigo.cocktailmanual.domain.entity.DrinkEntity {
        return mhelrigo.cocktailmanual.domain.entity.DrinkEntity(
            dateModified = drinkDatabaseEntity.dateModified,
            idDrink = drinkDatabaseEntity.idDrink.toString(),
            strAlcoholic = drinkDatabaseEntity.strAlcoholic,
            strCategory = drinkDatabaseEntity.strCategory,
            strCreativeCommonsConfirmed = drinkDatabaseEntity.strCreativeCommonsConfirmed,
            strDrink = drinkDatabaseEntity.strDrink,
            strDrinkAlternate = drinkDatabaseEntity.strDrinkAlternate,
            strDrinkThumb = drinkDatabaseEntity.strDrinkThumb,
            strGlass = drinkDatabaseEntity.strGlass,
            strIBA = drinkDatabaseEntity.strIBA,
            strImageAttribution = drinkDatabaseEntity.strImageAttribution,
            strImageSource = drinkDatabaseEntity.strImageSource,
            strIngredient1 = drinkDatabaseEntity.strIngredient1,
            strIngredient10 = drinkDatabaseEntity.strIngredient10,
            strIngredient11 = drinkDatabaseEntity.strIngredient11,
            strIngredient12 = drinkDatabaseEntity.strIngredient12,
            strIngredient13 = drinkDatabaseEntity.strIngredient13,
            strIngredient14 = drinkDatabaseEntity.strIngredient14,
            strIngredient15 = drinkDatabaseEntity.strIngredient15,
            strIngredient2 = drinkDatabaseEntity.strIngredient2,
            strIngredient3 = drinkDatabaseEntity.strIngredient3,
            strIngredient4 = drinkDatabaseEntity.strIngredient4,
            strIngredient5 = drinkDatabaseEntity.strIngredient5,
            strIngredient6 = drinkDatabaseEntity.strIngredient6,
            strIngredient7 = drinkDatabaseEntity.strIngredient7,
            strIngredient8 = drinkDatabaseEntity.strIngredient8,
            strIngredient9 = drinkDatabaseEntity.strIngredient9,
            strInstructions = drinkDatabaseEntity.strInstructions,
            strInstructionsDE = drinkDatabaseEntity.strInstructionsDE,
            strInstructionsES = drinkDatabaseEntity.strInstructionsES,
            strInstructionsFR = drinkDatabaseEntity.strInstructionsFR,
            strInstructionsIT = drinkDatabaseEntity.strInstructionsIT,
            strInstructionsZH_HANS = drinkDatabaseEntity.strInstructionsZH_HANS,
            strInstructionsZH_HANT = drinkDatabaseEntity.strInstructionsZH_HANT,
            strMeasure1 = drinkDatabaseEntity.strMeasure1,
            strMeasure10 = drinkDatabaseEntity.strMeasure10,
            strMeasure11 = drinkDatabaseEntity.strMeasure11,
            strMeasure12 = drinkDatabaseEntity.strMeasure12,
            strMeasure13 = drinkDatabaseEntity.strMeasure13,
            strMeasure14 = drinkDatabaseEntity.strMeasure14,
            strMeasure15 = drinkDatabaseEntity.strMeasure15,
            strMeasure2 = drinkDatabaseEntity.strMeasure2,
            strMeasure3 = drinkDatabaseEntity.strMeasure3,
            strMeasure4 = drinkDatabaseEntity.strMeasure4,
            strMeasure5 = drinkDatabaseEntity.strMeasure5,
            strMeasure6 = drinkDatabaseEntity.strMeasure6,
            strMeasure7 = drinkDatabaseEntity.strMeasure7,
            strMeasure8 = drinkDatabaseEntity.strMeasure8,
            strMeasure9 = drinkDatabaseEntity.strMeasure9,
            strTags = drinkDatabaseEntity.strTags,
            strVideo = drinkDatabaseEntity.strVideo,
            isFavourite = drinkDatabaseEntity.isFavourite
        )
    }

    fun transform(drinksApiEntity: DrinksApiEntity): DrinksEntity {
        return DrinksEntity(drinksApiEntity.drinkEntities)
    }
}