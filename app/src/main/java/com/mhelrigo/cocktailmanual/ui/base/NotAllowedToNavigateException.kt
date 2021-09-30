package com.mhelrigo.cocktailmanual.ui.base

class NotAllowedToNavigateException :
    Throwable("Not allowed to navigate", null, false, false) {

    companion object Factory{
        fun checkIfAllowedToNavigate(isTablet: Boolean) {
            if (isTablet) {
                throw NotAllowedToNavigateException()
            }

            return
        }
    }
}