package com.mhelrigo.domain

import mhelrigo.cocktailmanual.domain.model.*

class TestData {
    companion object Factory {
        fun provideDrinks(): List<Drink> = listOf(
            provideDrink("11007", "Margarita"),
            provideDrink("15395", "1-900-FUK-MEUP"),
            provideDrink("13581", "410 Gone")
        )

        private fun provideDrink(
            id: String, name: String
        ) = Drink(
            dateModified = "dateModified",
            idDrink = id,
            strAlcoholic = "dateModified",
            strCategory = "dateModified",
            strCreativeCommonsConfirmed = "dateModified",
            strDrink = name,
            strDrinkAlternate = "dateModified",
            strDrinkThumb = "dateModified",
            strGlass = "dateModified",
            strIBA = "dateModified",
            strImageAttribution = "dateModified",
            strImageSource = "dateModified",
            strIngredient1 = "dateModified",
            strIngredient10 = "dateModified",
            strIngredient11 = "dateModified",
            strIngredient12 = "dateModified",
            strIngredient13 = "dateModified",
            strIngredient14 = "dateModified",
            strIngredient15 = "dateModified",
            strIngredient2 = "dateModified",
            strIngredient3 = "dateModified",
            strIngredient4 = "dateModified",
            strIngredient5 = "dateModified",
            strIngredient6 = "dateModified",
            strIngredient7 = "dateModified",
            strIngredient8 = "dateModified",
            strIngredient9 = "dateModified",
            strInstructions = "dateModified",
            strInstructionsDE = "dateModified",
            strInstructionsES = "dateModified",
            strInstructionsFR = "dateModified",
            strInstructionsIT = "dateModified",
            strInstructionsZH_HANS = "dateModified",
            strInstructionsZH_HANT = "dateModified",
            strMeasure1 = "dateModified",
            strMeasure10 = "dateModified",
            strMeasure11 = "dateModified",
            strMeasure12 = "dateModified",
            strMeasure13 = "dateModified",
            strMeasure14 = "dateModified",
            strMeasure15 = "dateModified",
            strMeasure2 = "dateModified",
            strMeasure3 = "dateModified",
            strMeasure4 = "dateModified",
            strMeasure5 = "dateModified",
            strMeasure6 = "dateModified",
            strMeasure7 = "dateModified",
            strMeasure8 = "dateModified",
            strMeasure9 = "dateModified",
            strTags = "dateModified",
            strVideo = "dateModified"
        )

        fun provideCategories(): Categories =
            Categories(
                listOf(
                    provideCategory("Cocktail"),
                    provideCategory("Cocoa"),
                    provideCategory("Shot")
                )
            )

        private fun provideCategory(name: String): Category = Category(name)

        fun provideDrinkTypes(): DrinkTypes =
            DrinkTypes(listOf(provideDrinkType(""), provideDrinkType(""), provideDrinkType("")))

        private fun provideDrinkType(type: String): DrinkType = DrinkType(type)

        fun provideGlasses(): Glasses =
            Glasses(listOf(provideGlass(""), provideGlass(""), provideGlass("")))

        private fun provideGlass(glass: String): Glass = Glass(glass)

        fun provideIngredients(): Ingredients =
            Ingredients(
                listOf(
                    provideIngredient("Light rum"),
                    provideIngredient("Applejack"),
                    provideIngredient("Gin")
                )
            )

        private fun provideIngredient(ingredient: String): Ingredient =
            Ingredient(
                strIngredient = ingredient,
                idIngredient = "Lorem Ipsum",
                strABV = "Lorem Ipsum",
                strAlcohol = "Lorem Ipsum",
                strDescription = "Lorem Ipsum",
                strType = "Lorem Ipsum"
            )
    }
}