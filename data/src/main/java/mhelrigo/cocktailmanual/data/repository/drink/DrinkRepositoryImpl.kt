package mhelrigo.cocktailmanual.data.repository.drink

import kotlinx.coroutines.flow.*
import mhelrigo.cocktailmanual.data.mapper.DrinkEntityMapper
import mhelrigo.cocktailmanual.data.repository.drink.local.DrinkDao
import mhelrigo.cocktailmanual.data.repository.drink.remote.DrinkApi
import mhelrigo.cocktailmanual.domain.entity.DrinksEntity
import mhelrigo.cocktailmanual.domain.repository.DrinkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrinkRepositoryImpl @Inject constructor(
    var drinkApi: DrinkApi,
    var drinkDao: DrinkDao,
    var drinkEntityMapper: DrinkEntityMapper
) : DrinkRepository {
    override suspend fun getRandomly(): Flow<List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>> =
        flow {
            emit(drinkApi.getRandomly())
        }.map {
            drinkEntityMapper.transform(it)
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(t1, t2)
        }.map { collection -> markFavorites(collection) }

    override suspend fun getByPopularity(): Flow<List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>> =
        flow {
            emit(drinkApi.getByPopularity())
        }.map {
            drinkEntityMapper.transform(it)
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(t1, t2)
        }.map { collection -> markFavorites(collection) }

    override suspend fun getLatest(): Flow<List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>> =
        flow {
            emit(drinkApi.getLatest())
        }.map {
            drinkEntityMapper.transform(it)
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(t1, t2)
        }.map { collection -> markFavorites(collection) }

    override suspend fun searchByName(name: String): Flow<List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>> =
        flow {
            emit(drinkApi.searchByName(name))
        }.map {
            drinkEntityMapper.transform(it)
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(t1, t2)
        }.map { collection -> markFavorites(collection) }

    override suspend fun searchByIngredient(ingredient: String): Flow<List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>> =
        flow {
            emit(drinkApi.searchByIngredient(ingredient))
        }.map {
            drinkEntityMapper.transform(it)
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(t1, t2)
        }.map { collection -> markFavorites(collection) }

    override suspend fun getDetails(id: String): Flow<List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>> =
        flow {
            emit(drinkApi.getDetails(id))
        }.map {
            drinkEntityMapper.transform(it)
        }.zip(selectAllFavorite()) { t1, t2 ->
            Pair(t1, t2)
        }.map { collection -> markFavorites(collection) }

    override suspend fun addFavoriteById(drinkEntity: mhelrigo.cocktailmanual.domain.entity.DrinkEntity) {
        drinkDao.insert(drinkEntityMapper.transform(drinkEntity))
    }

    override suspend fun removeFavoriteById(drinkEntity: mhelrigo.cocktailmanual.domain.entity.DrinkEntity) {
        drinkDao.delete(drinkEntityMapper.transform(drinkEntity))
    }

    override suspend fun selectAllFavorite(): Flow<List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>> =
        flow {
            emit(drinkDao.selectAllFavorites().map { drinkEntityMapper.transform(it) })
        }

    private fun markFavorites(collection: Pair<DrinksEntity, List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity>>): List<mhelrigo.cocktailmanual.domain.entity.DrinkEntity> {
        if (collection.first == null || collection.second == null) {
            return emptyList()
        }

        val toBeMarked = collection.first.drinkEntities
        val favorites = collection.second

        favorites.map { v0 ->
            toBeMarked.filter { v1 -> v0.idDrink.equals(v1.idDrink) }.map {
                it.markFavorite(true)
            }
        }

        return toBeMarked
    }
}