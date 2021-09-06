package com.mhelrigo.cocktailmanual.ui.drink.viewholders

import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.ItemDrinkRegularBinding
import com.mhelrigo.cocktailmanual.ui.OnItemClickListener
import com.mhelrigo.cocktailmanual.ui.model.Drink

class RegularViewHolder(var view: ItemDrinkRegularBinding) : BaseViewHolder(view.root) {
    override fun bind(
        drink: Drink,
        onFavoritesToggled: OnItemClickListener<Drink>,
        onItemClicked: OnItemClickListener<Drink>
    ) {
        drink.bindingAdapterPosition = bindingAdapterPosition

        view.textViewName.text = drink.strDrink
        view.textViewCategory.text = "${drink.strCategory} and ${drink.strAlcoholic}"

        Glide.with(view.root.context).load(drink.strDrinkThumb).diskCacheStrategy(
            DiskCacheStrategy.ALL
        ).into(view.imageViewThumbnail)

        setUpFavoriteIcon(drink.returnIconForFavorite())

        view.imageViewFavorite.setOnClickListener {
            onFavoritesToggled.onClick(drink)
        }

        view.root.setOnClickListener {
            onItemClicked.onClick(drink)
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