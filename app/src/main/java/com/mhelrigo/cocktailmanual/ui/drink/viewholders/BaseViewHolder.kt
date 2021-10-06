package com.mhelrigo.cocktailmanual.ui.drink.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mhelrigo.cocktailmanual.model.DrinkModel
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(
        drink: DrinkModel,
        toggleFavorite: MutableSharedFlow<DrinkModel>,
        expandItem: MutableSharedFlow<DrinkModel>
    )
}