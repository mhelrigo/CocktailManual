package com.mhelrigo.cocktailmanual.ui.drink.viewholders

import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.ItemDrinkSmallBinding
import com.mhelrigo.cocktailmanual.model.DrinkModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class SmallViewHolder(var view: ItemDrinkSmallBinding) : BaseViewHolder(view.root) {
    override fun bind(
        drink: DrinkModel,
        toggleFavorite: MutableSharedFlow<DrinkModel>,
        expandItem: MutableSharedFlow<DrinkModel>
    ) {
        drink.bindingAdapterPosition = bindingAdapterPosition

        view.textViewName.text = drink.strDrink

        Glide.with(view.root.context).load(drink.strDrinkThumb).diskCacheStrategy(
            DiskCacheStrategy.ALL
        ).into(view.imageViewThumbnail)

        setUpFavoriteIcon(drink.returnIconForFavorite())

        view.imageViewFavorite.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                toggleFavorite.emit(drink)
            }
        }

        view.root.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                expandItem.emit(drink)
            }
        }
    }

    private fun setUpFavoriteIcon(resourceDrawable: Int) {
        view.imageViewFavorite.setImageDrawable(
            ResourcesCompat.getDrawable(
                view.root.resources,
                resourceDrawable,
                null
            )
        )
    }
}