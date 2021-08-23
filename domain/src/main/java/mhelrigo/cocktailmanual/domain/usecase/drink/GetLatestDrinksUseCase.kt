package mhelrigo.cocktailmanual.domain.usecase.drink

import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLatestDrinksUseCase @Inject constructor(
    val drinkRepository: DrinkRepository
) :
    UseCase<ResultWrapper<Exception, Drinks>, List<Any>>() {
    override suspend fun buildExecutable(params: List<Any>?): ResultWrapper<Exception, Drinks> {
        return drinkRepository.getLatest()
    }

    /*private suspend fun merge(): Drinks {
        val favorites = drinkRepository.selectAllFavorite()
        var latest = drinkRepository.getLatest()
        var favoriteWithLatest: List<Drink> = emptyList()

        if (favorites is ResultWrapper.Success && latest is ResultWrapper.Success) {
            favoriteWithLatest = (latest.value.drinks + favorites.value).groupingBy {
                it.idDrink
            }.reduce { _, accumulator, element ->
                element.copy(
                    isFavourite = element.isFavourite,
                    idDrink = accumulator.idDrink,
                    strDrink = accumulator.strDrink,
                    strCategory = accumulator.strCategory,
                    strAlcoholic = accumulator.strAlcoholic,
                    strDrinkThumb = accumulator.strDrinkThumb
                )
            }.values.toList()
        }

        return Drinks(favoriteWithLatest)
    }*/
}