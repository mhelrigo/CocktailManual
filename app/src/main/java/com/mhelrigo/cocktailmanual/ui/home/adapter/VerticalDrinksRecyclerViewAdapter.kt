package com.mhelrigo.cocktailmanual.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mhelrigo.cocktailmanual.databinding.ItemDrink2Binding
import com.mhelrigo.cocktailmanual.ui.model.Drink

class VerticalDrinksRecyclerViewAdapter(
    var onFavoritesToggled: OnItemClickListener<Drink>,
    var onItemClicked: OnItemClickListener<Drink>,
) : RecyclerView.Adapter<VerticalDrinksRecyclerViewAdapter.ViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<Drink>() {
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.idDrink?.toInt() == newItem.idDrink?.toInt()
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.strDrink == newItem.strDrink && oldItem.idDrink?.toInt() == newItem.idDrink?.toInt()
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    fun toggleFavoriteOfADrink(position: Int, drink: Drink) {
        differ.currentList.find { a -> a.idDrink == drink.idDrink }?.isFavourite = drink.isFavourite
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VerticalDrinksRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            ItemDrink2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: VerticalDrinksRecyclerViewAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(var view: ItemDrink2Binding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(drink: Drink) {
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
}