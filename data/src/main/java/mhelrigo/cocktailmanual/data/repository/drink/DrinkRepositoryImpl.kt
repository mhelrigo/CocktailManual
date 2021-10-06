package mhelrigo.cocktailmanual.data.repository.drink

import com.mhelrigo.commons.DISPATCHERS_IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import mhelrigo.cocktailmanual.data.mapper.DrinkEntityMapper
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
    var drinkEntityMapper: DrinkEntityMapper,
    @Named(DISPATCHERS_IO) var ioCoroutineContext: CoroutineContext
) : DrinkRepository {
    override suspend fun getRandomly(): Flow<List<mhelrigo.cocktailmanual.domain.model.Drink>> =
        flow {
            emit(drinkApi.getRandomly())
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(
                t1,
                t2
            )
        }.map { collection -> markFavorites(collection) }

    override suspend fun getByPopularity(): Flow<List<mhelrigo.cocktailmanual.domain.model.Drink>> =
        flow {
            emit(drinkApi.getByPopularity())
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(
                t1,
                t2
            )
        }.map { collection -> markFavorites(collection) }

    override suspend fun getLatest(): Flow<List<mhelrigo.cocktailmanual.domain.model.Drink>> =
        flow {
            emit(drinkApi.getLatest())
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(
                t1,
                t2
            )
        }.map { collection -> markFavorites(collection) }

    override suspend fun searchByFirstLetter(firstLetter: String): ResultWrapper<Exception, Drinks> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByFirstLetter(firstLetter) }
        }

    override suspend fun searchByName(name: String): Flow<List<mhelrigo.cocktailmanual.domain.model.Drink>> =
        flow {
            emit(drinkApi.searchByName(name))
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(
                t1,
                t2
            )
        }.map { collection -> markFavorites(collection) }

    override suspend fun searchByIngredient(ingredient: String): Flow<List<mhelrigo.cocktailmanual.domain.model.Drink>> =
        flow {
            emit(drinkApi.searchByIngredient(ingredient))
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(
                t1,
                t2
            )
        }.map { collection -> markFavorites(collection) }

    override suspend fun searchByDrinkType(drinkType: String): ResultWrapper<Exception, Drinks> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByDrinkType(drinkType) }
        }

    override suspend fun searchByCategory(category: String): ResultWrapper<Exception, Drinks> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByCategory(category) }
        }

    override suspend fun searchByGlass(glass: String): ResultWrapper<Exception, Drinks> =
        withContext(ioCoroutineContext) {
            ResultWrapper.build { drinkApi.searchByGlass(glass) }
        }

    override suspend fun getDetails(id: String): Flow<List<mhelrigo.cocktailmanual.domain.model.Drink>> =
        flow {
            emit(drinkApi.getDetails(id))
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(
                t1,
                t2
            )
        }.map { collection -> markFavorites(collection) }

    override suspend fun addFavoriteById(drink: mhelrigo.cocktailmanual.domain.model.Drink) {
        drinkDao.insert(drinkEntityMapper.transform(drink))
    }

    override suspend fun removeFavoriteById(drink: mhelrigo.cocktailmanual.domain.model.Drink) {
        drinkDao.delete(drinkEntityMapper.transform(drink))
    }

    override suspend fun selectAllFavorite(): Flow<List<mhelrigo.cocktailmanual.domain.model.Drink>> =
        flow {
            emit(drinkDao.selectAllFavorites().map { drinkEntityMapper.transform(it)})
        }

    private fun markFavorites(collection: Pair<Drinks, List<mhelrigo.cocktailmanual.domain.model.Drink>>): List<mhelrigo.cocktailmanual.domain.model.Drink> {
        val toBeMarked = collection.first.drinks
        val favorites = collection.second

        favorites.map { v0 ->
            toBeMarked.filter { v1 -> v0.idDrink.equals(v1.idDrink) }.map {
                it.markFavorite(true)
            }
        }

        return toBeMarked
    }
}