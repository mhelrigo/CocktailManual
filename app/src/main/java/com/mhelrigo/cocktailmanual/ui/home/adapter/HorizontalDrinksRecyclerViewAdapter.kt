package com.mhelrigo.cocktailmanual.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.mhelrigo.cocktailmanual.databinding.ItemLatestDrinkBinding
import com.mhelrigo.cocktailmanual.ui.model.Drink
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class HorizontalDrinksRecyclerViewAdapter(
    var onFavoritesToggled: OnItemClickListener<Drink>,
    var onItemClicked: OnItemClickListener<Drink>
) :
    RecyclerView.Adapter<HorizontalDrinksRecyclerViewAdapter.ViewHolder>() {
    private val _drinks = ArrayList<Drink>()

    fun submit(drinks: List<Drink>) {
        _drinks.clear()
        _drinks.addAll(drinks)
        notifyDataSetChanged()
    }

    fun toggleFavoriteOfADrink(position: Int, drink: Drink) {
        _drinks.find { a -> a.idDrink == drink.idDrink }?.isFavourite = drink.isFavourite
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalDrinksRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            ItemLatestDrinkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: HorizontalDrinksRecyclerViewAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(_drinks[position])
    }

    override fun getItemCount(): Int {
        return _drinks.size
    }

    inner class ViewHolder(var view: ItemLatestDrinkBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(drink: Drink) {
            drink.bindingAdapterPosition = bindingAdapterPosition

            view.textViewName.text = drink.strDrink
            view.textViewCategory.text = drink.strCategory
            view.textViewType.text = drink.strAlcoholic

            Picasso.get().load(drink.strDrinkThumb).transform(CropCircleTransformation())
                .into(view.imageView)

            setUpFavoriteIcon(drink.returnIconForFavorite())
            setUpBackgroundColor(drink)

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

        private fun setUpBackgroundColor(drink: Drink) {
            view.cardViewContainer.backgroundTintList =
                ResourcesCompat.getColorStateList(
                    view.root.resources,
                    drink.backGroundColorDrawableColor,
                    null
                )
        }
    }
}