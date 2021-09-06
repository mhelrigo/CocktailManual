package com.mhelrigo.cocktailmanual.ui.drink.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.model.Drink

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(
        drink: Drink,
        onFavoritesToggled: OnItemClickListener<Drink>,
        onItemClicked: OnItemClickListener<Drink>
    )
}