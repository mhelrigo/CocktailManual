package mhelrigo.cocktailmanual.data.mapper

import mhelrigo.cocktailmanual.data.entity.ingredient.IngredientsApiEntity
import mhelrigo.cocktailmanual.domain.entity.IngredientDetailEntity
import mhelrigo.cocktailmanual.domain.entity.IngredientsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientMapper @Inject constructor(){
    fun transformList(ingredients: IngredientsApiEntity) : IngredientsEntity {
        return IngredientsEntity(ingredients.ingredientEntity)
    }

    fun transform(ingredient: IngredientsApiEntity) : IngredientDetailEntity {
        return IngredientDetailEntity(ingredient.ingredientEntity)
    }
}