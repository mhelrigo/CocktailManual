package mhelrigo.cocktailmanual.data.repository.drink

import kotlinx.coroutines.withContext
import mhelrigo.cocktailmanual.data.model.Drink
import mhelrigo.cocktailmanual.data.repository.drink.local.DrinkDao
import mhelrigo.cocktailmanual.data.repository.drink.remote.DrinkApi
import mhelrigo.cocktailmanual.domain.model.Drinks
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import mhelrigo.cocktailmanual.domain.usecase.base.ResultWrapper
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class DrinkRepositoryImpl @Inject constructor(
    var drinkApi: DrinkApi,
    var drinkDao: DrinkDao,
    @Named("Dispatchers.IO") var ioCoroutineContext: CoroutineContext
) : DrinkRepository {
    override suspend fun getRandomly(): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.getRandomly() }
        }

    override suspend fun getByPopularity(): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.getByPopularity() }
        }

    override suspend fun getLatest(): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.getLatest() }
        }

    override suspend fun searchByFirstLetter(firstLetter: String): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByFirstLetter(firstLetter) }
        }

    override suspend fun searchByName(name: String): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByName(name) }
        }

    override suspend fun searchByIngredient(ingredient: String): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByIngredient(ingredient) }
        }

    override suspend fun searchByDrinkType(drinkType: String): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByDrinkType(drinkType) }
        }

    override suspend fun searchByCategory(category: String): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByCategory(category) }
        }

    override suspend fun searchByGlass(glass: String): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByGlass(glass) }
        }

    override suspend fun getDetails(id: String): ResultWrapper<Drinks, Exception> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.getDetails(id) }
        }

    override suspend fun addFavoriteById(id: String): ResultWrapper<Unit, Exception> {
        return try {
            withContext(ioCoroutineContext) {
                ResultWrapper.build { drinkDao.insert(Drink(uid = id as Int, idDrink = id)) }
            }
        } catch (e: Exception) {
            ResultWrapper.build { throw Exception("Insert failed") }
        }
    }

    override suspend fun removeFavoriteById(id: String): ResultWrapper<Unit, Exception> {
        return try {
            withContext(ioCoroutineContext) {
                ResultWrapper.build { drinkDao.delete(Drink(uid = id as Int, idDrink = id)) }
            }
        } catch (e: Exception) {
            ResultWrapper.build { throw Exception("Insert failed") }
        }
    }
}