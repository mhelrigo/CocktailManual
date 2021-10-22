package com.mhelrigo.cocktailmanual.ui.drink

import androidx.lifecycle.MutableLiveData
import com.mhelrigo.cocktailmanual.model.DrinkModel
import com.mhelrigo.cocktailmanual.ui.commons.ViewStateWrapper
import kotlinx.coroutines.flow.flow
import timber.log.Timber

interface SyncDrinks {
    /**
     * Modifies given lists of drinks when a given drink's [DrinkModel.isFavourite] changes.
     * It does so by iterating through the list until it matches given drink's [DrinkModel.idDrink]
     *
     * @param p0 - The Drink
     * @param p1 - The list of Drinks
     * @return Boolean - If Drink exist will return True, else will return false.
     * */
    fun sync(p0: DrinkModel, p1: List<MutableLiveData<ViewStateWrapper<List<DrinkModel>>>>, p2: Int) =  flow {
        if (p1[p2].value == null) {
            emit(false)
        }

        if (p1[p2].value is ViewStateWrapper.Success) {
            val v1: List<DrinkModel> =
                (p1[p2].value as ViewStateWrapper.Success<List<DrinkModel>>).data

            val index = v1.indexOf(v1.find { a -> a.idDrink == p0.idDrink })

            if (index > -1) {
                val v3: MutableList<DrinkModel> = mutableListOf<DrinkModel>()

                v1.map {
                    v3.add(it.copy())
                }

                v3[index].markFavorite(p0.isFavourite)

                p1[p2].postValue(ViewStateWrapper.Success(v3))

                emit(true)
            }

            emit(false)
        }

        emit(false)
    }
}