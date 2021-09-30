package com.mhelrigo.cocktailmanual.ui.drink

import android.os.Bundle
import androidx.navigation.NavController
import com.mhelrigo.cocktailmanual.ui.base.NotAllowedToNavigateException
import com.mhelrigo.cocktailmanual.ui.base.NotAllowedToNavigateException.Factory.checkIfAllowedToNavigate
import timber.log.Timber

/**
 * Not all fragments needs to navigate to @MealDetailFragment,
 * only the ones implementing this interface will.
 */
interface DrinkNavigator {
    fun navigateToDrinkDetail(
        action: Int,
        bundle: Bundle?,
        navController: NavController,
        isTablet: Boolean
    ) {
        try {
            checkIfAllowedToNavigate(isTablet)
        } catch (e: NotAllowedToNavigateException) {
            Timber.e("You're not allowed to navigate when device is tablet")
            return
        }

        bundle?.let {
            navController.navigate(action)
            return
        } ?: run {
            navController.navigate(action, bundle)
        }
    }
}